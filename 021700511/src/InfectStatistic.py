import argparse
import os
import re
import sys
import locale
import functools
from datetime import date as _date

locale.setlocale(locale.LC_COLLATE, 'zh_CN.UTF-8')


def get_date(year, month, day):
    return _date(int(year), int(month), int(day))


def parse_argument():
    parser = argparse.ArgumentParser(description='疫情统计程序')
    subparsers = parser.add_subparsers(help='sub-command help')
    parser_a = subparsers.add_parser('list', allow_abbrev=False)
    parser_a.add_argument('-log', help="指定日志目录的位置", required=True)
    parser_a.add_argument('-out', help="指定输出文件路径和文件名", required=True)
    parser_a.add_argument('-date', help="指定日期")
    parser_a.add_argument('-type', nargs='*', help="[ip： 感染患者，sp：疑似患者，cure：治愈 ，dead：死亡患者]")
    parser_a.add_argument('-province', nargs='*', help="指定列出的省")
    parser_a.set_defaults(func=InfectStatistic.execute_from_args)
    _args = parser.parse_args()
    return _args


class InfectStatistic:

    @staticmethod
    def execute_from_args(args):
        i = InfectStatistic(args.log, args.out, args.date, args.type, args.province)
        i.read_and_out_to_file()

    def __init__(self, logs_path, out_path, date=None, allow_types=None, province=None):
        self.allow_types = allow_types or ['ip', 'sp', 'cure', 'dead']  # 输出的数据类型
        self.allow_provinces = province or []  # 输出的省份
        self.deadline = None  # 日志截止日期
        self.out_path = ""  # 输出文件
        self.logs_list = {}  # 日志文件
        self.data = {}  # 数据  格式为：{province: {ip: num, sp: num, cure: num, dead: num}}

        # 计算日期合法性
        try:
            self.deadline = get_date(*date.split('-', 2)) if date else None
        except (ValueError, TypeError):
            print('日期 %s 非法' % date)
            sys.exit('error-date')

        # 计算路径合法性
        self._check_logs_path(logs_path)
        self._check_out_path(out_path)

        # 日志日期范围检测
        if self.deadline and self.deadline > max(self.logs_list.keys()):
            print('%s 超出日志给出的范围' % self.deadline)
            sys.exit('error-date-range')

    def _check_logs_path(self, logs_path):
        if not os.path.isdir(logs_path):
            print('日志目录: %s 不是正确的目录' % logs_path)
            sys.exit('error-log-dir')
        for filename in os.listdir(logs_path):
            file_path = os.path.join(logs_path, filename)
            if os.path.isfile(file_path):
                result = re.match(r'(\d+)-(\d+)-(\d+).log.txt', filename)
                if result:
                    try:
                        date = get_date(*result.group(1, 2, 3))
                    except ValueError:
                        continue  # 忽略日期不正确的文件
                    self.logs_list.update({date: file_path})
        if not self.logs_list:
            print('日志目录为空')
            sys.exit('error-log-null')

    def _check_out_path(self, out_path):
        try:
            open(out_path, 'w').close()
            self.out_path = out_path
        except (FileNotFoundError, PermissionError):
            print('输出路径 %s 不正确' % out_path)
            exit('error-out')

    def _new_province(self, province):
        self.data.update({province: {"ip": 0, "sp": 0, "cure": 0, "dead": 0}})

    def _add_people(self, province, num, _type, _sub=False):
        num = -int(num) if _sub else int(num)
        if province not in self.data:
            self._new_province(province)
        self.data[province][_type] += num

    # 解析日志中的一行
    def _parse_line(self, line):
        if line.startswith('//'):
            return
        _patterns = [
            ('(.*?) 新增 感染患者 ([0-9]+)人', (((1, 2), 'ip', False),)),
            ('(.*?) 新增 疑似患者 ([0-9]+)人', (((1, 2), 'sp', False),)),
            ('(.*?) 感染患者 流入 (.*?) ([0-9]+)人', (((1, 3), 'ip', True), ((2, 3), 'ip', False))),
            ('(.*?) 疑似患者 流入 (.*?) ([0-9]+)人', (((1, 3), 'sp', True), ((2, 3), 'sp', False))),
            ('(.*?) 死亡 ([0-9]+)人', (((1, 2), 'dead', False), ((1, 2), 'ip', True))),
            ('(.*?) 治愈 ([0-9]+)人', (((1, 2), 'cure', False), ((1, 2), 'ip', True))),
            ('(.*?) 疑似患者 确诊感染 ([0-9]+)人', (((1, 2), 'sp', True), ((1, 2), 'ip', False))),
            ('(.*?) 排除 疑似患者 ([0-9]+)人', (((1, 2), 'sp', True),)),
        ]
        for _pattern, _args_list in _patterns:
            result = re.match(_pattern, line)
            if result:
                for _args in _args_list:
                    index, _type, _sub = _args
                    province, num = result.group(*index)
                    self._add_people(province, num, _type, _sub)
                return

    # 解析日志文件列表
    def _read_log(self):
        for date, file_path in self.logs_list.items():
            if self.deadline and date > self.deadline:
                continue
            with open(file_path, mode='r', encoding='utf-8') as log:
                for line in log.readlines():
                    self._parse_line(line.strip())
                log.close()

    def _write_str(self, province, province_data):
        type_dict = {
            "ip": "感染患者%s人",
            "sp": "疑似患者%s人",
            "cure": "治愈%s人",
            "dead": "死亡%s人"
        }
        out_str = province + " "
        for _type in self.allow_types:
            if _type in type_dict:
                out_str += (type_dict[_type] % province_data[_type]) + " "
        return out_str

    def _out_to_file(self, data):
        with open(self.out_path, 'w', encoding='utf8') as out_file:
            if '全国' in data:
                out_file.writelines(self._write_str('全国', data['全国']) + '\n')
                data.pop('全国')
            province_list = list(data.keys())
            province_list.sort(key=functools.cmp_to_key(locale.strcoll))
            for province in province_list:
                out_file.writelines(self._write_str(province, data[province]) + '\n')
            out_file.writelines('// 该文档并非真实数据，仅供测试使用\n')

    # 计算全国总数据
    def _all_data(self):
        data = {"ip": 0, "sp": 0, "cure": 0, "dead": 0}
        for province in self.data:
            data['ip'] += self.data[province]['ip']
            data['sp'] += self.data[province]['sp']
            data['cure'] += self.data[province]['cure']
            data['dead'] += self.data[province]['dead']
        self.data.update({'全国': data})

    def _get_out_data(self):
        out_data = {}
        # 获取省份列表 并排序
        if self.allow_provinces:
            province_list = list(set(self.allow_provinces))
        else:
            province_list = list(self.data.keys())
        # 计算全国总量
        if not self.allow_provinces or '全国' in self.allow_provinces:
            self._all_data()
            province_list.append('全国')

        # 生成数据字典
        for province in province_list:
            if province not in self.data:
                self._new_province(province)
            province_data = {}
            for out_type in self.allow_types:
                try:
                    province_data.update({out_type: self.data[province][out_type]})
                except KeyError:
                    continue
            out_data.update({province: province_data})

        return out_data

    def read_and_out_to_file(self):
        self._read_log()
        data = self._get_out_data()
        self._out_to_file(data)


if __name__ == "__main__":
    args = parse_argument()
    args.func(args)

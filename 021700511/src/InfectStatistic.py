import argparse
import os
import re
import sys
from datetime import date as _date


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
    args = parser.parse_args()
    return vars(args)


class InfectStatistic:

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
            sys.exit(0)
        # 计算路径合法性
        self._check_logs_path(logs_path)
        self._check_out_path(out_path)

        # 日志日期范围检测
        if self.deadline and self.deadline > max(self.logs_list.keys()):
            print('%s 超出日志给出的范围' % self.deadline)
            sys.exit()

    def _check_logs_path(self, logs_path):
        if not os.path.isdir(logs_path):
            print('日志目录: %s 不是正确的目录' % logs_path)
            sys.exit(0)
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
            sys.exit()

    def _check_out_path(self, out_path):
        try:
            open(out_path, 'w').close()
            self.out_path = out_path
        except (FileNotFoundError, PermissionError):
            print('输出路径 %s 不正确' % out_path)
            exit(0)

    def _new_province(self, province):
        self.data.update({province: {"ip": 0, "sp": 0, "cure": 0, "dead": 0}})

    def _add_people(self, re_result, result_index, _type, _sub=False):
        province, num = re_result.group(*result_index)
        num = -int(num) if _sub else int(num)
        if province not in self.data:
            self._new_province(province)
        self.data[province][_type] += num

    # 解析日志中的一行
    def _parse_line(self, line):
        _patterns = [
            ('(.*?) 新增 感染患者 ([0-9]+)人', ((self._add_people, (1, 2), 'ip'),)),
            ('(.*?) 新增 疑似患者 ([0-9]+)人', ((self._add_people, (1, 2), 'sp'),)),
            ('(.*?) 感染患者 流入 (.*?) ([0-9]+)人', ((self._add_people, (1, 3), 'ip', True),
                                               (self._add_people, (2, 3), 'ip'),)),
            ('(.*?) 疑似患者 流入 (.*?) ([0-9]+)人', ((self._add_people, (1, 3), 'sp', True),
                                               (self._add_people, (2, 3), 'sp'),)),
            ('(.*?) 死亡 ([0-9]+)人', ((self._add_people, (1, 2), 'dead'),
                                    (self._add_people, (1, 2), 'ip', True),)),
            ('(.*?) 治愈 ([0-9]+)人', ((self._add_people, (1, 2), 'cure'),
                                    (self._add_people, (1, 2), 'ip', True),)),
            ('(.*?) 疑似患者 确诊感染 ([0-9]+)人', ((self._add_people, (1, 2), 'sp', True),
                                           (self._add_people, (1, 2), 'ip'),)),
            ('(.*?) 排除 疑似患者 ([0-9]+)人', ((self._add_people, (1, 2), 'sp', True),)),
        ]
        for _pattern, _funcs in _patterns:
            result = re.match(_pattern, line)
            if result:
                for _func, *_arg in _funcs:
                    _arg.insert(0, result)
                    _func(*_arg)
                return

    # 解析日志文件列表
    def _read_log(self):
        for date, file_path in self.logs_list.items():
            if self.deadline and date > self.deadline:
                continue
            log = open(file_path, mode='r', encoding='utf-8')
            try:
                for line in log.readlines():
                    self._parse_line(line.strip())
            finally:
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
        out_file = open(self.out_path, 'w', encoding='utf8')
        try:
            if '全国' in data:
                out_file.writelines(self._write_str('全国', data['全国']) + '\n')
                data.pop('全国')
            province_list = list(data.keys())
            province_list.sort(reverse=True)
            for province in province_list:
                out_file.writelines(self._write_str(province, data[province]) + '\n')
            out_file.writelines('// 该文档并非真实数据，仅供测试使用\n')
        finally:
            out_file.close()

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

    def read_and_out_to_object(self):
        self._read_log()
        return self._get_out_data()


if __name__ == "__main__":
    args = parse_argument()
    i = InfectStatistic(args['log'], args['out'], args['date'], args['type'], args['province'])
    i.read_and_out_to_file()

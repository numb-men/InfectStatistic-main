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

    def __init__(self, log, out, date=None, allow_types=None, province=None):
        # 配置
        self.allow_types = allow_types or ['ip', 'sp', 'cure', 'dead']
        self.allow_provinces = province
        self.deadline = None
        if date:
            try:
                date = get_date(*date.split('-', 2))
                self.deadline = date
            except (ValueError, TypeError):
                print('日期 %s 非法' % date)
                sys.exit(0)
        self.out_path = out
        self.logs_path = log
        self.log_list = {}
        self._check_path()

        # 数据类
        self.ip = {}  # 感染患者 {province: num}
        self.sp = {}  # 疑似患者 {province: num}
        self.cure = {}  # 治愈 {province: num}
        self.dead = {}  # 死亡 {province: num}
        self._list = (self.ip, self.sp, self.cure, self.dead)

    def _check_path(self):
        if not os.path.isdir(self.logs_path):
            print('日志目录: %s 不是正确的目录' % self.logs_path)
            sys.exit(0)
        for filename in os.listdir(self.logs_path):
            file_path = os.path.join(self.logs_path, filename)
            if os.path.isfile(file_path):
                result = re.match(r'(\d+)-(\d+)-(\d+).log.txt', filename)
                if result:
                    try:
                        date = get_date(*result.group(1, 2, 3))
                    except ValueError:
                        continue
                    self.log_list.update({date: file_path})
        # 范围检测
        if self.deadline and self.deadline > max(self.log_list.keys()):
            print('%s 超出日志给出的范围' % self.deadline)
            sys.exit()

        try:
            open(self.out_path, 'w').close()
        except (FileNotFoundError, PermissionError):
            print('输出路径 %s 不正确' % self.out_path)
            exit(0)

    def _add_people(self, re_result, result_index, _type, _sub=False):
        _province, _num = re_result.group(*result_index)
        _dic = {
            "ip": self.ip,
            "sp": self.sp,
            "cure": self.cure,
            "dead": self.dead
        }[_type]
        _num = -int(_num) if _sub else int(_num)
        if _province not in _dic:
            _dic[_province] = 0
        _dic[_province] += _num

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
        for date, file_path in self.log_list.items():
            if self.deadline and date > self.deadline:
                continue
            log = open(file_path, mode='r', encoding='utf-8')
            try:
                for line in log.readlines():
                    self._parse_line(line.strip())
            finally:
                log.close()

    def _print(self, out_file, province, *num):
        type_dict = {
            "ip": "感染患者%s人" % num[0],
            "sp": "疑似患者%s人" % num[1],
            "cure": "治愈%s人" % num[2],
            "dead": "死亡%s人" % num[3]
        }
        out_str = province
        for out_type in self.allow_types:
            if out_type in type_dict:
                out_str += " " + type_dict[out_type]
        out_file.writelines(out_str + '\n')

    def _out(self):
        all_num = []
        province_list = set()
        out_file = open(self.out_path, 'w', encoding='utf8')
        try:
            for dic in self._list:
                all_num.append(sum(dic.values()))
                province_list.update(dic.keys())
            if self.allow_provinces:
                for p in self.allow_provinces:
                    if p != '全国':
                        province_list.add(p)
            if not self.allow_provinces or '全国' in self.allow_provinces:
                self._print(out_file, '全国', *all_num)
            province_list = list(province_list)
            province_list.sort(reverse=True)
            for province in province_list:
                if self.allow_provinces and province not in self.allow_provinces:
                    continue
                num_list = []
                for dic in self._list:
                    num_list.append(dic[province] if province in dic else 0)
                self._print(out_file, province, *num_list)
            out_file.writelines('// 该文档并非真实数据，仅供测试使用\n')
        finally:
            out_file.close()

    def read_and_out(self):
        self._read_log()
        self._out()


if __name__ == "__main__":
    args = parse_argument()
    i = InfectStatistic(args['log'], args['out'], args['date'], args['type'], args['province'])
    i.read_and_out()

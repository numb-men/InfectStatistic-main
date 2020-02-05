import argparse
import os
import re
import sys
from datetime import date as Date


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
                year, month, day = date.split('-', 2)
                date = Date(int(year), int(month), int(day))
                self.deadline = date
            except (ValueError, TypeError):
                print('日期 %s 非法' % date)
                sys.exit(0)
        self.out_path = out
        self.logs_path = log
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
            exit(0)

        try:
            open(self.out_path, 'w').close()
        except (FileNotFoundError, PermissionError):
            print('输出路径 %s 不正确' % self.out_path)
            exit(0)

    # TODO 待优化
    # 解析日志中的一行
    def _parse_line(self, line):
        _list = [self.ip, self.sp, self.dead, self.cure]
        _pattern = [
            '(.*?) 新增 感染患者 ([0-9]+)人',
            '(.*?) 新增 疑似患者 ([0-9]+)人',
        ]
        for i in range(len(_pattern)):
            pattern = _pattern[i]
            result = re.match(pattern, line)
            if result:
                province, num = result.group(1, 2)
                dic = _list[i]
                dic[province] = int(dic[province]) + int(num) if province in dic else int(num)
                return
        _pattern = [
            '(.*?) 感染患者 流入 (.*?) ([0-9]+)人',
            '(.*?) 疑似患者 流入 (.*?) ([0-9]+)人'
        ]
        for i in range(len(_pattern)):
            pattern = _pattern[i]
            result = re.match(pattern, line)
            if result:
                province_out, province_in, num = result.group(1, 2, 3)
                dic = _list[i]
                dic[province_out] = int(dic[province_out]) - int(num) if province_out in dic else -int(num)
                dic[province_in] = int(dic[province_in]) + int(num) if province_in in dic else int(num)
                return
        _pattern = [
            '(.*?) 死亡 ([0-9]+)人',
            '(.*?) 治愈 ([0-9]+)人',
        ]
        _list = [self.dead, self.cure]
        for i in range(len(_pattern)):
            pattern = _pattern[i]
            result = re.match(pattern, line)
            if result:
                province, num = result.group(1, 2)
                dic = _list[i]
                dic[province] = int(dic[province]) + int(num) if province in dic else int(num)
                self.ip[province] = int(self.ip[province]) - int(num) if province in self.ip else -int(num)
                return
        result = re.match('(.*?) 疑似患者 确诊感染 ([0-9]+)人', line)
        if result:
            province, num = result.group(1, 2)
            self.ip[province] = int(self.ip[province]) + int(num) if province in self.ip else int(num)
            self.sp[province] = int(self.sp[province]) - int(num) if province in self.sp else -int(num)
            return
        result = re.match('(.*?) 排除 疑似患者 ([0-9]+)人', line)
        if result:
            province, num = result.group(1, 2)
            self.sp[province] = int(self.sp[province]) - int(num) if province in self.sp else -int(num)
            return

    # 解析日志文件列表
    def _read_log(self):
        for filename in os.listdir(self.logs_path):
            file_path = os.path.join(self.logs_path, filename)
            if os.path.isfile(file_path):
                result = re.match(r'(\d+)-(\d+)-(\d+).log.txt', filename)
                if result:
                    try:
                        year, month, day = result.group(1, 2, 3)
                        date = Date(int(year), int(month), int(day))
                    except ValueError:
                        continue
                    if self.deadline and date > self.deadline:
                        continue
                    log = open(file_path, mode='r', encoding='utf-8')
                    for line in log.readlines():
                        self._parse_line(line.strip())

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
        out_file.writelines(out_str+'\n')

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


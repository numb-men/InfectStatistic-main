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
    parser_a.add_argument('-date', nargs='*', help="指定日期")
    parser_a.add_argument('-type', nargs='*', help="[ip： 感染患者，sp：疑似患者，cure：治愈 ，dead：死亡患者]")
    parser_a.add_argument('-province', nargs='*', help="指定列出的省")
    args = parser.parse_args()
    return vars(args)


class InfectStatistic:

    def __init__(self, log, out, dates=None, allow_types=None, province=None):
        # 配置类
        self.logs_path = log
        self.out_path = out
        self.allow_types = allow_types
        self.province = province
        self.dates = []
        if dates:
            try:
                for date in dates:
                    year, month, day = date.split('-', 2)
                    date = Date(int(year), int(month), int(day))
                    self.dates.append(date)
            except (ValueError, TypeError):
                print('日期 %s 非法' % dates)
                sys.exit(0)

        # 数据类
        self.ip = {}  # 感染患者 {province: num}
        self.sp = {}  # 疑似患者 {province: num}
        self.cure = {}  # 治愈 {province: num}
        self.dead = {}  # 死亡 {province: num}

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


    # 解析日志
    def _read_log(self):
        logs_path = self.logs_path
        if os.path.exists(logs_path) and os.path.isdir(logs_path):
            for filename in os.listdir(logs_path):
                file_path = os.path.join(logs_path, filename)
                if os.path.isfile(file_path):
                    result = re.match(r'(\d+)-(\d+)-(\d+).log.txt', filename)
                    if result:
                        try:
                            year, month, day = result.group(1, 2, 3)
                            date = Date(int(year), int(month), int(day))
                        except ValueError:
                            continue
                        if self.dates and date not in self.dates:
                            continue
                        log = open(file_path, mode='r', encoding='utf-8')
                        for line in log.readlines():
                            self._parse_line(line.strip())
        else:
            print('不存在目录 %s ' % logs_path)
            sys.exit()

    def _print(self, province, *num):
        type_dict = {
            "ip": "感染患者%s人" % num[0],
            "sp": "疑似患者%s人" % num[1],
            "cure": "治愈%s人" % num[2],
            "dead": "死亡%s人" % num[3]
        }
        out_str = province
        for out_type in type_dict:
            if not self.allow_types or out_type in self.allow_types:
                out_str += " " + type_dict[out_type]
        print(out_str)

    def out(self):
        _list = [self.ip, self.sp, self.cure, self.dead]
        all_num = []
        province_list = set()
        for dic in _list:
            all_num.append(sum(dic.values()))
            province_list.update(dic.keys())
        if self.province:
            for p in self.province:
                if p != '全国':
                    province_list.add(p)
        if not self.province or '全国' in self.province:
            self._print('全国', *all_num)
        for province in province_list:
            if self.province and province not in self.province:
                continue
            num_list = []
            for dic in _list:
                num_list.append(dic[province] if province in dic else 0)
            self._print(province, *num_list)


if __name__ == "__main__":
    args = parse_argument()
    i = InfectStatistic(args['log'], args['out'], args['date'], args['type'], args['province'])
    i._read_log()
    i.out()



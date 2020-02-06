from .src.InfectStatistic import InfectStatistic
import unittest
import os

basedir = r'D:\Workspace\Python\InfectStatistic-main\021700511'

_test_log = os.path.join(basedir, 'log\\test_log\\')


def get_result_name(result_name):
    _test_result = os.path.join(basedir, 'result\\test_result\\' + result_name)
    return _test_result


class TestInfectStatistic(unittest.TestCase):

    def test_all_log(self):
        i = InfectStatistic(_test_log, get_result_name('test_result1.txt'), None, None, None)
        i.read_and_out()
        test_result_file = open(get_result_name('test_result1.txt'), 'r', encoding='utf-8')
        result_file = open(get_result_name('result1.txt'), 'r', encoding='utf-8')
        test_result = test_result_file.readlines()
        result = result_file.readlines()
        self.assertEqual(len(result), len(test_result), '输出文件行数不一致')
        for i in range(min(len(result), len(test_result))):
            self.assertEqual(result[i].strip(), test_result[i].strip(), '第 {} 行 内容不一致'.format(i+1))


if __name__ == "__main__":
    unittest.main()
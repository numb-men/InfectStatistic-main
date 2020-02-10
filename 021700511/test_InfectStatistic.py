try:
    from .src.InfectStatistic import InfectStatistic
except ModuleNotFoundError:
    from src.InfectStatistic import InfectStatistic
# 由于模块机制，在pycharm中使用自带的单元测试引入方式应该为
# from .src.InfectStatistic import InfectStatistic
# 若直接运行本函数，则使用
# from src.InfectStatistic import InfectStatistic
import unittest
import os
import warnings

basedir = r'D:\Workspace\Python\InfectStatistic-main\021700511'

_test_log = os.path.join(basedir, 'log\\test_log\\')


def get_result_name(result_name):
    _test_result = os.path.join(basedir, 'result\\test_result\\' + result_name)
    return _test_result


class TestInfectStatistic(unittest.TestCase):
    def setUp(self) -> None:
        warnings.simplefilter('ignore', ResourceWarning)

    def _compare_file(self, file_right, file_test):
        file_right_line = file_right.readlines()
        file_test_line = file_test.readlines()
        self.assertEqual(len(file_right_line), len(file_test_line), '输出文件行数不一致')
        for i in range(min(len(file_right_line), len(file_test_line))):
            self.assertEqual(file_right_line[i].strip(), file_test_line[i].strip(),
                             '第 {} 行 内容不一致'.format(i + 1))

    def _test_case(self, logs_path, right_out_name, out_name, date=None, allows_types=None,
                   province=None):
        i = InfectStatistic(logs_path, get_result_name(out_name), date, allows_types, province)
        i.read_and_out_to_file()
        with open(get_result_name(right_out_name), 'r', encoding='utf-8') as file_right, \
                open(get_result_name(out_name), 'r', encoding='utf-8') as file_test:
            self._compare_file(file_right, file_test)

    def test_default(self):
        self._test_case(_test_log, 'result1.txt', 'test_out.txt', None, None, None)

    def test_date_1(self):
        self._test_case(_test_log, 'result2.txt', 'test_out.txt', '2020-01-22', None, None)

    def test_date_2(self):
        self._test_case(_test_log, 'result3.txt', 'test_out.txt', '2020-01-23', None, None)

    def test_types_1(self):
        self._test_case(_test_log, 'result4.txt', 'test_out.txt', None, ['sp', 'dead', 'cure'],
                        None)

    def test_types_2(self):
        self._test_case(_test_log, 'result5.txt', 'test_out.txt', None, ['cure', 'ip'], None)

    def test_types_3_with_error_type(self):
        self._test_case(_test_log, 'result5.txt', 'test_out.txt', None, ['cure', 'ip', 'er'], None)

    def test_province_1(self):
        self._test_case(_test_log, 'result6.txt', 'test_out.txt', None, None, ['全国', '新疆'])

    def test_province_2(self):
        self._test_case(_test_log, 'result7.txt', 'test_out.txt', None, None, ['浙江', '湖北'])

    def test_province_3(self):
        self._test_case(_test_log, 'result8.txt', 'test_out.txt', None, None, ['福建'])

    def test_all_args_1(self):
        self._test_case(_test_log, 'result9.txt', 'test_out.txt', '2020-01-22', ['sp', 'ip'],
                        ['福建'])

    def test_all_args_2(self):
        self._test_case(_test_log, 'result10.txt', 'test_out.txt', '2020-01-27', ['dead'], ['全国'])

    def test_error_date(self):
        with self.assertRaises(SystemExit) as err:
            self._test_case(_test_log, 'result2.txt', 'test_out.txt', '2020-00-21', None, None)
        self.assertEqual(err.exception.args[0], 'error-date')

    def test_error_date_range(self):
        with self.assertRaises(SystemExit) as err:
            self._test_case(_test_log, 'result2.txt', 'test_out.txt', '2020-02-22', None, None)
        self.assertEqual(err.exception.args[0], 'error-date-range')

    def test_error_log_dir(self):
        with self.assertRaises(SystemExit) as err:
            self._test_case(_test_log + '\\abs', 'result2.txt', 'test_out.txt', None, None, None)
        self.assertEqual(err.exception.args[0], 'error-log-dir')

    def test_error_log_null(self):
        with self.assertRaises(SystemExit) as err:
            self._test_case('d:', 'result2.txt', 'test_out.txt', None, None, None)
        self.assertEqual(err.exception.args[0], 'error-log-null')

    def test_error_out(self):
        with self.assertRaises(SystemExit) as err:
            i = InfectStatistic(_test_log, 'e:', None, None, None)
        self.assertEqual(err.exception.args[0], 'error-out')


if __name__ == "__main__":
    unittest.main()
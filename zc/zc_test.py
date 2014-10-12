#!/usr/bin/env python 

import unittest
import zc

class ZCtest(unittest.TestCase):
    def test_get_all_buyarrs(self):
        buy_max, length = 2, 3
	expected = [[1,1,1], [1,1,2], [1,2,1], [1,2,2],[2,1,1], [2,1,2],[2,2,1]]

	actual = []
	for buy_arr in zc.get_all_buyarrs(buy_max, length):
	    actual.append(buy_arr)
	self.assertListEqual(expected, actual)

if __name__=='__main__':
    unittest.main()


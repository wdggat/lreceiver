#!/usr/bin/python 

import unittest
import spider_shengpingfu

class SpiderTest(unittest.TestCase):
    def test_decode(self):
        testf = 'shengpingfu_2s1_20141011.html'
        games = spider_shengpingfu.decode(open(testf))
	self.assertListEqual([1,], games)

if __name__=='__main__':
    unittest.main()

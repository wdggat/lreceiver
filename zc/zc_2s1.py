#!/usr/bin/env python

import sys
from profit import Profit
from datetime import datetime
import sp_checker
import zc

BUY_MAX = 7

#def _get_all_buyarrs(buy_max):
#    for b00 in range(1, buy_max+1):
#        for b01 in range(1, buy_max+1):
#            for b02 in range(1, buy_max+1):
#                for b10 in range(1, buy_max+1):
#                    for b11 in range(1, buy_max+1):
#                        for b12 in range(1, buy_max+1):
#                            for b20 in range(1, buy_max+1):
#                                for b21 in range(1, buy_max+1):
#                                    for b22 in range(1, buy_max+1):
#				        yield [b00, b01, b02, b10, b11, b12, b20, b21, b22]
    
def main(sysar): 
    topN = 100 
    sp_arra = [float(sysar[i]) for i in (0,1,2)]
    sp_arrb = [float(sysar[i]) for i in (3,4,5)]
    sp_arr = []
    for sp_a in sp_arra:
        for sp_b in sp_arrb:
	    sp_arr.append(sp_a * sp_b)

    print 'sp_a: %s, sp_b: %s' % (sp_arra, sp_arrb)
    profits = zc.get_topN_profit(sp_arr, topN)
    for profit in profits:
        print str(profit)

if __name__=='__main__':
    main(sys.argv[1:])


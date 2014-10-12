#!/usr/bin/env python

import sys
from profit import Profit
from datetime import datetime
import sp_checker
    
BUY_MAX = 100

#def _get_all_buyarrs(buy_max):
#    for goal0 in range(1, buy_max+1):
#        for goal1 in range(1, buy_max+1):
#            for goal2 in range(1, buy_max+1):
#                for goal3 in range(1, buy_max+1):
#                    for goal4 in range(1, buy_max+1):
#                        for goal5 in range(1, buy_max+1):
#                            for goal6 in range(1, buy_max+1):
#                                for goal7_plus in range(1, buy_max+1):
#                                    yield [goal0, goal1, goal2, goal3, goal4, goal5, goal6, goal7_plus]

def _get_all_buyarrs(buy_max):
    for goal0 in range(1, buy_max+1):
        for goal1 in range(1, buy_max+1):
            for goal2 in range(1, buy_max+1):
	        yield [goal0, goal1, goal2]

def get_topN_profit(sp_arr, topN):
    if any([True for sp in sp_arr if sp <= 0]):
        print 'someone negative, drop.'
        return []

#    s, legal = sp_checker.check(sp_arr)
#    print '%s sp_check.value: %f' % (sp_arr, s)
#    if not legal:
#        print 'sp useless, drop'
#	return []

    profits, c = [], 0
    for buy_arr in _get_all_buyarrs(BUY_MAX):
	c += 1
	if c % 10000000 == 0:
	    print 'c : %d' % c
        profit = Profit.get_from_sparr(sp_arr, buy_arr, multiple=1)
	if not profit.qualified():
	    continue
	else:
	    profits.append(profit)
    print 'all_positive.len = %d' % len(profits)
    sorted_profits = sorted(profits, reverse=True)
    return sorted_profits[0:topN]

def main(sysargv):
    topN = 200
    sp_arr = [float(i) for i in sysargv]
    print 'SP: %s' % sp_arr
    profits = get_topN_profit(sp_arr, topN)
    for profit in profits:
        print str(profit)

if __name__=='__main__':
    main(sys.argv[1:])


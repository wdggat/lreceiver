#!/usr/bin/python 

import sys
import zc

def print_usage():
    print 'Usage:'
    print '\t./run.py dan <SP_ARR>'
    print '\t./run.py 2s1 <SP_ARR>'

def danchang(sp_arr):
    profits = zc.get_topN_profit(sp_arr, 200, profit_multiple=1,buy_max=8)
    for profit in profits:
        print str(profit)

def _2s1(sps):
    sp_arra, sp_arrb = sps[0:len(sps) / 2], sps[len(sps)/2:]
    profits = zc.get_topN_profit_2s1(sp_arra, sp_arrb, 200, buy_max=7)
    for profit in profits:
        print str(profit)

def score(sp_arr):
    profits = zc.get_topN_profit(sp_arr, 20, profit_multiple=2,buy_max=4, buy_min=1)
    for profit in profits:
        print str(profit)

def main(argv):
    jobname = argv[1]
    sps = [float(sp) for sp in argv[2:]]
    if jobname == 'dan':
        return danchang(sps)
    elif jobname == '2s1':
        return _2s1(sps)
    elif jobname == 'score':
        return score(sps)
    else:
        return print_usage()

if __name__=='__main__':
    main(sys.argv)


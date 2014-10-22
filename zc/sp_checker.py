#!/usr/bin/env python
#-*-encoding: utf-8-*-

"""
an -> 倍数, sn -> sp值
a1s1, a2s2, a3s3 ... ansn
cost: 2 * ( a1 + a2 + ... + an)
如果需要任意sn命中，都有盈利 ->  2*aksk >= 2 * (a1 + a2 + ... + an) -> 1/sk <= ak/(a1 + a2 + ... + an)
->  1/s1 + 1/s2 + ... + 1/sn <= 1 (必要不充分条件)
"""

import sys

def check(sp_arr):
    s = sum([ 1 / float(sp) for sp in sp_arr])
    print 'sp check value: %f' % s
    return s,s <= 1

if __name__=='__main__':
    sps = [float(i) for i in sys.argv[1:]]
    s,legal = check(sps)
    print s,legal


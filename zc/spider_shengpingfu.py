#!/usr/bin/python 

import re
from bs4 import BeautifulSoup
import urllib

URL='http://caipiao.163.com/order/preBet_jczqspfmixp.html'

def decode(content):
    soup = BeautifulSoup(content)
    gametree = soup.find_all(isstop="0")
    #print 'gametree: %s' % gametree
    games = []
    for game in gametree:
        hostname = game['hostname'].encode('utf-8')
	guestname = game['guestname'].encode('utf-8')
	leaguename = game['leaguename'].encode('utf-8')
	starttime = int(game['starttime']) / 1000
	#attached_flight.find('span', {'class' : 'base_price'}).text
        col_concede = game.find('span', {'class' : 'co5 towLine'}).findAll('em')
	#print 'col_concede: %s' % col_concede
	concede1 = col_concede[0].text.encode('utf-8')
	concede2 = col_concede[1].text.encode('utf-8')
	sps = []
	col_sp = game.find('span', {'class' : 'co6 btnBox towLine '})
	#print 'col_sp: %s' % col_sp
	for spem in col_sp.findAll('em'):
	    if spem == None or not spem.has_attr('sp'):
	        break
	    sps.append(float(spem['sp'].encode('utf-8')))
	if len(sps) <= 5:
	    continue
	g = [hostname, guestname, leaguename, starttime, concede1, concede2]
	g.extend(sps)
	games.append(g)
    return games

def spide():
    content = urllib.urlopen(URL).read()
    return decode(content)

def main():
    #content = open('shengpingfu_2s1_20141011.html')
    for game in spide():
        print '\t'.join([str(g) for g in game])

if __name__ == '__main__':
    main()

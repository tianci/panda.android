#!/usr/bin/env python
# -*- coding: utf-8 -*-
    
import csv
import os
import re
import sys
import zipfile
import xml.etree.ElementTree as ElementTree

'''自动生成相应的dimens.xml文件'''
if __name__ == '__main__':
    # print "srcipt：", sys.argv[0]
    # for i in range(1, len(sys.argv)):
    #     print "argv[", i , "] = ", sys.argv[i]
        
    # 目标尺寸
    desWidth = 720;
    desHeight = 1280;
    
    # 采用layout_size 发现标准layout的最大宽高（单位：dp）
    maxWidth = 1280;
    maxHeight = 720;
    
    
    # 输出合理的dimens.xml文件
    density = 0.5 #1.0 * maxWidth / desWidth;  #屏幕密度
    for i in range(desWidth):
        j = i + 1
        # print "<dimen name=\"ws_%d_%d_%s\">%.2fdp</dimen>" % (desWidth, desHeight, j, 1.0 * j * density)
        print "<dimen name=\"width_%d_%d_%s\">%.2fdp</dimen>" % (desWidth, desHeight, j, 1.0 * j * density)
        
    density = 0.5 #1.0 * maxHeight / desHeight
    for i in range(desHeight):
        j = i + 1
        # print "<dimen name=\"hs_%d_%d_%s\">%.2fdp</dimen>" % (desWidth, desHeight, j, 1.0 * j * density)
        print "<dimen name=\"height_%d_%d_%s\">%.2fdp</dimen>" % (desWidth, desHeight, j, 1.0 * j * density)
        
    density = 0.5 #1.0 * maxHeight / desHeight
    for i in range(90):
        j = i + 1
        # print "<dimen name=\"ts_%d_%d_%s\">%.2fsp</dimen>" % (desWidth, desHeight, j, 1.0 * j * density)
        print "<dimen name=\"text_size_%d_%d_%s\">%.2fsp</dimen>" % (desWidth, desHeight, j, 1.0 * j * density)
    
        
    

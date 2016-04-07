#!/usr/bin/env python
# -*- coding: utf-8 -*-
    
import csv
import os
import re
import sys
import time

def buildApk():
    os.system("../gradlew :PandaAndroidDemo:clean")
    os.system("../gradlew :PandaAndroidDemo:build")
    return
  
def generateApk():
    buildApk();
    dirname, filename = os.path.split(os.path.abspath(sys.argv[0]))	
    #print "dirname = " , dirname
    #print "filename = " , filename
    project_name =  dirname.split('/')[-1]
    sourceFile = "./build/outputs/apk/"+project_name+"-release.apk"
    if os.path.isfile(sourceFile): 
        print "文件" , sourceFile , ", 生成成功                       \n"
    else:
        print "文件" , sourceFile , ", 生成失败                       \n" 

# 在gradle去配置参数
if __name__ == '__main__':
    print "srcipt：", sys.argv[0]
    #for i in range(1, len(sys.argv)):
    #    print "argv[", i ,"] = ", sys.argv[i]
    generateApk();

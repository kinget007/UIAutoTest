#!/bin/bash
#dateFileName=`date +"%Y%m%d%H%M%S"`
#dateFileName=`date +"%Y%m%d"`
#timeFileName=`date +"%H%S"`
date=`cat date.txt |cut -d : -f1`
time=`cat date.txt |cut -d : -f2`
currentPath=`pwd`

java -classpath "target/test-classes/" -Djava.ext.dirs=lib org.testng.TestNG -suitethreadpoolsize 1 TestSuits/APP_*_{devices}_*.xml -d output/${date}/${time}/{udid}/testngReports

cd tools/testngReport
ant transform -Din=${currentPath}/output/${date}/${time}/{udid}/testngReports/testng-results.xml -Dout=${currentPath}/output/${date}/${time}/{udid}/testngReports/index_xslt.html -Dexpression=${currentPath}/output/${date}/${time}/{udid}/testngReports/
#cd  ${currentPath}/output/{date}/{time}/{udid}/testngReports
#if [  -d "lastbuild" ]; then
#  rm  -rf  lastbuild
#fi
#mkdir lastbuild
#cp -rf ./${timeFileName}/* ./lastbuild

#cd ${currentPath}/tools
#echo "junitreports_FilePath = " ../output/testngReports/${platform}/${dateFileName}/junitreports
#java -jar GenerateTestNgReport.jar ../output/testngReports/${platform}/${dateFileName} ${syscflag}  old
#java -jar GenerateTestNgReport.jar ${currentPath}/output/testngReports/${dateFileName} {isSynchronizedData}
#cd ../../../
#cp -r output  ./../../userContent/$BUILD_NUMBER
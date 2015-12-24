#!/bin/bash
isSynchronizedData=$1
isCreateReport=$2
date=`cat date.txt |cut -d : -f1`
time=`cat date.txt |cut -d : -f2`
currentPath=`pwd`

cd ${currentPath}/tools
java -jar GenerateTestNgReport.jar ${currentPath}/output/${date}/${time} ${isSynchronizedData} ${isCreateReport}

cd $currentPath/output/report
mv -f reportAll.html ${currentPath}/output/${date}/${time}/

cd ${currentPath}/output/
if [  -d "lastbuild" ]; then
rm  -rf  lastbuild
fi
mkdir lastbuild
cp -rf ${currentPath}/output/${date}/${time}/* ${currentPath}/output/lastbuild/

cd $currentPath
rm -rf $currentPath/autoTest_*
cp -rf output  ./../../userContent/$BUILD_NUMBER
ps -A|grep node|grep -v grep|awk 'NR=1 {print $1}'|xargs kill -9

devices=`adb devices|awk 'NR!=1 {print $1}'|xargs`
for device in $devices
do
echo "start reboot device >>" $device
adb -s $device reboot
done
echo `date +"%Y%m%d:%H%M"` > date.txt
sh prepare.sh
java -jar BatchRunAppUI.jar
sh handleResult.sh y y

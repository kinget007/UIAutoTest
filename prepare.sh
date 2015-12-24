ps -A|grep node|grep -v grep|awk 'NR=1 {print $1}'|xargs kill -9
. /Users/apple/.bash_profile
. /etc/profile
. /etc/bashrc
mvn clean;mvn test-compile
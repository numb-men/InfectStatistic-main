set pr_name=Hanani-Chen
git pull
git checkout -b %pr_name%-master master
git pull https://github.com/%pr_name%/InfectStatistic-main.git master
del .gitignore
git add .
git commit -m"merge conflicts"
git checkout master
git merge --no-ff %pr_name%-master
git push origin master
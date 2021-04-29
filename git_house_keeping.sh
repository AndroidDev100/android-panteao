echo "Make user You are on branch QA while running this house keeping"

echo "git fetch origin"
git fetch origin

echo "############# GIT House Keeping : MERGED BRANCHES ############"
for branch in `git branch -r --merged | grep -v HEAD`;do echo -e `git log --no-merges -n 1 --format="%ci, %cr, %an, %ae, "  $branch | head -n 1` \\t$branch; done | sort -r

echo "==================================================================="
echo ""

echo "############# GIT House Keeping : NON-MERGED BRANCHES ############"
for branch in `git branch -r --no-merged | grep -v HEAD`;do echo -e `git log --no-merges -n 1 --format="%ci, %cr, %an, %ae, " $branch | head -n 1` \\t$branch; done | sort -r


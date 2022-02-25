# Style Guide

1. When writing css, always start with a prefix like `cft-` to avoid conflicts
with CSS in other projects when integrating vue component.
2. Import the components in relative order.
3. When naming new Components follow Pascal-case.
4. Follow the [VueJS Style Guide when developing components](https://vuejs.org/v2/style-guide/).
5. On developing your code, follow the [Eslint rules specified](../.eslintrc.js)

## Guidelines for raising PRs

1. There are two protected branches: master and dev. dev is for continuous development
and master branch as stable branch for the project which contains released version code.
2. For any new changes raise PR to dev branch first.
3. Always squash merge the Pull requests(PR) you raise with proper commit message except
in case of PR from dev branch to master branch for an upcoming releases.

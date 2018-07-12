# Contributing Guidelines
The core development team (hereafter "us" and "we") require all contributors to adhere to the standards and guidelines defined in this document. They have been designed to ensure consistency and quality in both the source code and the derivative artifacts.

The guidelines/standards cover the following topics:
- Reporting issues and suggesting improvements.
- Committing, branching, and tagging.
- Pull requests.
- Coding standards.

## Reporting issues and suggesting improvements
We would prefer to reserve the issues section for enhancement ideas and legitimate issues. To this end, we do not allow issues which are simply requests for assistance. You're more likely to get a better response if you post your problem on Stack Overflow and tweet us with a link.

Before opening an issue, please ensure:
- A duplicate issue is not currently open.
- The issue is not covered by an existing, broader issue.
- The issue has not already been fixed in a newer build.

Sometimes regressions happen, so if you find an issue that was previously fixed but has happened again, please comment on the original issue so that it can be reopened.

We maintain this repository in our free time, so any time we spend managing the issues section is time we could otherwise spend having fun or adding cool features. Please keep this in mind when opening a new issue.

## Committing, branching, and tagging
We require all commits, branches and tags to adhere to a strict set of standards. These standards enhance the readability of the project history, and they allow us to automatically generate the changelog for each release.

### Commits
All commit messages are made up of two sections: a mandatory header and an optional body. The header consists of a single line only, whereas the body can span across multiple lines if required. The header and the body are separated by a single blank line, however this line is omitted if the body is not present. Commit messages are structured as:
```
$type($scope): $subject

$body
```

`$type` must be one of the following:
- `feat`: Added a new feature.
- `fix`: Fixed an existing issue or bug.
- `docs`: Changed the documentation.
- `style`: Changed the code in a way that does not affect the meaning of the code (e.g. formatting for readability).
- `refactor`: Changed the code in a way that is neither a bugfix nor a new feature, but still affected the meaning of the code.
- `test`: Added, changed or removed tests.
- `build`: Modified the build system.
- `misc`: Made changes not covered by the other types. Misc changes are not included in the changelogs.

Some common ambiguous tasks:
- Changes to the README and other checked in repository documents are docs changes.
- Adding/removing/updating the external dependencies are build changes.
- Adding/updating copyright headers are misc changes.

`$scope` denotes the feature, layer or place where the change was made. The scope is subject to the following rules:
- Use only lower case letters and hyphens.
- Use at most three words.
- Separate each word with a single hyphen.
- Never start or end with a hyphen.

`$subject` is a succinct description of the changes in the commit. Consider it both a commit title and a single changelog entry. When writing the subject:
- Use past tense.
- Don't capitalise the first letter.
- Don't end with a full stop or another punctuation mark.
- Don't repeat the scope.

If you find you're struggling to pick a tag, scope or subject, then you probably need to break the commit into multiple smaller commits.

`$body` is optional and can be omitted if desired. Each line of the body is structured as:
```
:$key $value
```

The body is subject to the following rules:
- Don't use blank lines.
- Start each new line with a key.
- Keys can be used in any order.
- Don't repeat keys.
- If you use a key it must have a non-empty value.
- Not every key must be used.
- Use standard English grammar for the value (e.g. start with a capital and end with a punctuation mark).
- Values may contain multiple sentences.
- Values must not contain new lines.

`$key` must be one of the following:
- `desc`: A description of the commit.
- `issue`: A comma-separated list of issue identifiers resolved by this commit.
- `break`: Description of any breaking changes introduced by this commit.

Full examples:
```
build(ui-library): moved deployment logic out of the main gradle script
```
```
feat(storyboard): added the dashboard view

:desc The view has been completely set up, but the controller still needs to be made.
```
```
fix(users): added salt to the username hash

:desc Added a salt column to the user table to increase security.
:issue CVE-14,NCC-74656
```
```
feat(user): replaced profile photo blob with URL in response

:issue GEN-23
:break The response no longer contains the 'userPhoto' property. Old apps will break.
```

### Branches
Each branch must adhere to the gitflow standards outlined [here](https://danielkummer.github.io/git-flow-cheatsheet/). To summarise:
- The `master` branch contains the current production code.
- The `develop` branch contains the current working version.
- Feature branches and bugfix branches are named as `feature/$title` and `bugfix/$title` respectively. Feature and bugfix branches originate from head of `develop` and are merged back into `develop` when finished.
- Release branches are named as `release/$variant-v$versionNumber` where `$variant-` is optional. Release branches originate from the head of `develop` and are merged into both `master` and `develop` when finished.
- Hotfix branches are named as `hotfix/$title`. Hotfix branches originate from the head of `master` and are merged into both `master` and `develop` when finished. Merging into `develop` is optional if the fix is no longer relevant.

In addition to the standard gitflow branches, we also use repodocs branches for changes that affect only the checked-in repository documentation (e.g. the README). These branches are named as `repodocs/$title`. They originate from the head of `master` and are merged into both `master` and `develop` when finished. Merging into `develop` is optional if the change is no longer relevant. A repodoc branch is essentially a hotfix branch that only touches repository documentation.

`$title` and `$variant` are subject to the following rules:
- Only use lower case letters and hyphens.
- Separate each word with a single hyphen.
- Never start or end with a hyphen.

`$version` is subject to the following rules:
- Only use lowercase letters, numbers, full stops, and hyphens.
- Separate each word with a single hyphen.
- Never start or end with a hyphen or a full stop.

### Tags
Tags are used for releases only. Whenever a release branch is merged into master, tag the merge commit with `$variant-v$version`, where `$variant` and `$version` match the name of the release branch. If the branch has no `-$variant` then omit it from the tag too.

## Pull requests
We reserve the right to reject any pull request that does not meet our standards. In particular, we will not accept pull requests that do not meet all the standards outlined in this document.

We don't want to waste anyone's time, so please check with us before you even start writing code. We might have already considered your idea and chosen not to pursue it for a good reason, or we might already be implementing it ourselves.

## Coding standards
We require all contributors to adhere to a strict set of coding standards. Consistent code increases the readability of the entire repository and makes development easier for all contributors. Some of these standards can be enforced by automated tools, but others must be enforced manually. The coding standards are documented [here](CodingStandards.md).
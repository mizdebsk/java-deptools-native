Name:		soft
Version:	1
Release:	1
Summary:	soft

License:	ASL 2.0
URL:		https://github.com/mizdebsk/java-deptools-native

BuildArch: noarch

Provides: test-Provides-A
Provides: test-Provides-B
Provides: test-Provides-C

Requires(interp): test-Requires-interp-A
Requires(interp): test-Requires-interp-B
Requires(interp): test-Requires-interp-C

Requires(rpmlib): test-Requires-rpmlib-A
Requires(rpmlib): test-Requires-rpmlib-B
Requires(rpmlib): test-Requires-rpmlib-C

Requires(verify): test-Requires-verify-A
Requires(verify): test-Requires-verify-B
Requires(verify): test-Requires-verify-C

Requires(pre): test-Requires-pre-A
Requires(pre): test-Requires-pre-B
Requires(pre): test-Requires-pre-C

Requires(post): test-Requires-post-A
Requires(post): test-Requires-post-B
Requires(post): test-Requires-post-C

Requires(preun): test-Requires-preun-A
Requires(preun): test-Requires-preun-B
Requires(preun): test-Requires-preun-C

Requires(postun): test-Requires-postun-A
Requires(postun): test-Requires-postun-B
Requires(postun): test-Requires-postun-C

Requires(pretrans): test-Requires-pretrans-A
Requires(pretrans): test-Requires-pretrans-B
Requires(pretrans): test-Requires-pretrans-C

Requires(posttrans): test-Requires-posttrans-A
Requires(posttrans): test-Requires-posttrans-B
Requires(posttrans): test-Requires-posttrans-C

Requires: test-Requires-A
Requires: test-Requires-B
Requires: test-Requires-C

Conflicts: test-Conflicts-A
Conflicts: test-Conflicts-B
Conflicts: test-Conflicts-C

Obsoletes: test-Obsoletes-A
Obsoletes: test-Obsoletes-B
Obsoletes: test-Obsoletes-C

Recommends: test-Recommends-A
Recommends: test-Recommends-B
Recommends: test-Recommends-C

Suggests: test-Suggests-A
Suggests: test-Suggests-B
Suggests: test-Suggests-C

Supplements: test-Supplements-A
Supplements: test-Supplements-B
Supplements: test-Supplements-C

Enhances: test-Enhances-A
Enhances: test-Enhances-B
Enhances: test-Enhances-C

OrderWithRequires: test-OrderWithRequires-A
OrderWithRequires: test-OrderWithRequires-B
OrderWithRequires: test-OrderWithRequires-C

%description
soft

%files

%changelog

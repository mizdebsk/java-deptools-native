Name:           rpmtags
Epoch:          4242424242
Version:        1
Release:        1
Summary:        Rpm tags
License:        CC0
BuildArch:      noarch

ExclusiveArch:  %{java_arches} noarch

BuildRequires:  bash
Provides:       which <= 10
Requires:       libstdc++ >= 10
Conflicts:      make = 2
Obsoletes:      rpmtags < 11
Suggests:       java-latest-openjdk > 8
Supplements:    tmt = 1
Enhances:       ant < 5
OrderWithRequires: maven >= 3

Recommends:     nethack
Recommends:     foo < 1
Recommends:     bar = 23-4.5
Recommends:     baz >= 3333333333:444444444444444444444-xaxaxayyyy.5517~77+8
Recommends:     ((ant and ivy) or maven >= 3.0.4)

%description
%{summary}.

%build

%install

%files

%changelog

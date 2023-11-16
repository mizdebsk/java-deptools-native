Name:           rpmtags
Epoch:          4242424242
Version:        1
Release:        1
Summary:        Rpm tags
License:        CC0
BuildArch:      noarch

ExclusiveArch:  %{java_arches} noarch

Recommends: nethack
Recommends: foo < 1
Recommends: bar = 23-4.5
Recommends: baz >= 3333333333:444444444444444444444-xaxaxayyyy.5517~77+8
Recommends: ((ant and ivy) or maven >= 3.0.4)

%description
%{summary}.

%build

%install

%files

%changelog

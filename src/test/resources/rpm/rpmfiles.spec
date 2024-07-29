Name:           rpmfiles
Version:        1
Release:        1
Summary:        Rpm files
License:        CC0
BuildArch:      noarch

%description
%{summary}.

%build

%install
cd %{buildroot}
mkdir -p b/a/se
echo content >b/a/se/file.txt
mkdir -p a/directory
touch a/directory/conf{1,2}
ln -s something symlink

%files
%dir /a/directory
%config /a/directory/conf1
%config(noreplace) /a/directory/conf2
/b
%ghost /gh/ost
/symlink

%changelog

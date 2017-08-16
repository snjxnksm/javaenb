# chocolateyに関して

Windows上で動く、バージョン管理システム。yumやapt-getに似たもの。  
以下は、インストールと簡単な取り扱い方の記述。

## インストール

1. 管理者でpoweshellを実行　　
  ```
  C:\Users\user01>powershell
  Windows PowerShell
  Copyright (C) 2009 Microsoft Corporation. All rights reserved.

  PS C:\Users\user01>
  ```
2. PowerShellスクリプトの実行ポリシーを確認  
  Unrestrictedにする。
  ```
	PS C:\Users\user01> get-executionpolicy
	Restricted
	PS C:\Users\user01>
	PS C:\Users\user01> set-executionpolicy  unrestricted
	PS C:\Users\user01>
	PS C:\Users\user01> get-executionpolicy
	Unrestricted
	PS C:\Users\user01>
  ```
3. Chocolateyインストール実行  
   警告出るけど気にしない。
  ```
  PS C:\Users\user01> iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
  ```

## パッケージインストール

インストールは `cinst パッケージ名` コマンドを使う。  
`choco install パッケージ名`でもいいが、短縮形のほうが早い。  
```
> cinst curl
```
インストール可能なパッケージ名は、以下で確認する。  
https://chocolatey.org/packages

## インストール済みのパッケージ一覧

ローカルにインストールされたパッケージの一覧を見るには。
```
> choco list -l
Chocolatey v0.10.5
Atom 1.0.7
chocolatey 0.10.5
chocolatey-core.extension 1.2.0
curl 7.52.1
git 2.12.2.2
git.install 2.12.2.2
jdk8 8.0.121
pgadmin3 1.22.2
sourcetree 1.9.10.0
wget 1.19.1
10 packages installed.
```

## パッケージアップデート

インストールされたパッケージをすべてUpadateするには以下。
```
> choco update all -y
```

## パッケージアンインストール

不要パッケージのアンインストールは以下。
```
> choco uninstall curl
```

#!/bin/bash

JARNAME=sample.jar
MAINCLASS=
MAINCLASSONEFILE=


# init
THIS="$0"
BINDIR=`dirname "$THIS"`
BINDIR=`cd "$BINDIR"; pwd`
BASEDIR=`cd "$BINDIR"/..; pwd`
TASKNAME="$1"
CONFDIR="$BASEDIR/conf"
LIBDIR="$BASEDIR/lib"

cd "$BASEDIR"

LANG=ja_JP.UTF-8
JAVA_HOME=/usr/local/java8

export LANG JAVA_HOME

MAIL_ADDRESS="";
ALERT_MAIL_SUBJECT="";
ALERT_MAIL_MESSAGE="";

PROGRAM_ID=;
DEL_PROGRAM_ID=;

### 関数 #####################
# ログ
_nm_info() {
        echo -e "`date '+%F %T'` INFO  ReceiveCreativeXml - $1" >> $BASEDIR/log/$PROGRAM_ID.log 2>&1;
}

# ロックファイル作成
_lock() {
        LOCKFILE="$1";
        # 存在チェック
        if [ -f "$LOCKFILE" ] ; then
                _nm_info "ロックファイルが既に存在。処理終了:$LOCKFILE";
                exit 0;
        fi
        _nm_info "ロックファイル作成:$LOCKFILE";
        touch "$LOCKFILE";
}

# ロックファイル削除
_unlock() {
        LOCKFILE="$1";
        _nm_info "ロックファイル削除:$LOCKFILE";
        rm "$LOCKFILE";
        if [ -f "$LOCKFILE" ] ; then
                _nm_info "ロックファイル削除失敗。処理終了:$LOCKFILE";
                exit 1;
        fi
}

# メール送信（メッセージ、題名、メールアドレス）#
_sendmail() {
	echo "$1" | sed -e 's/<br>/\n/g' | iconv -f UTF-8 -t SJIS | mail -s "$2" "$3";
}
##############################

cd "$BASEDIR"

## 登録
_nm_info "$PROGRAM_ID開始";

# ロックファイル作成
MAIN_LOCKFILE="$BASEDIR/$PROGRAM_ID.lock";
_lock $MAIN_LOCKFILE;

# 設定ファイルディレクトリとjarをクラスパスに追加
CLASSPATH=$CONFDIR
JARPATH="$LIBDIR/$JARNAME"
CLASSPATH="$CLASSPATH:$JARPATH"

# lib以下のjarファイルをクラスパスに追加
for i in "$LIBDIR"/*.jar ; do
        if [ "$JARPATH" != "$i" ] ; then
                CLASSPATH="$CLASSPATH":"$i"
        fi
done

export CLASSPATH


#キック
"$JAVA_HOME/bin/java" -Xmx4096m "$MAINCLASSONEFILE" $TASKNAME


EXEC_RESULT=$?
_nm_info "結果コード：$EXEC_RESULT";

# ロックファイル削除
_unlock $MAIN_LOCKFILE;

_nm_info "$PROGRAM_ID終了";


# エラーメール送信
TOTAL_RESULT=`expr $EXEC_RESULT`;
if [ $TOTAL_RESULT -ne 0 ] ; then
	_sendmail "$ALERT_MAIL_MESSAGE" "$ALERT_MAIL_SUBJECT" "$MAIL_ADDRESS";
fi

exit $TOTAL_RESULT;

#!/bin/bash
if [ -f ~/.lasrosasiot ]; then
  source ~/.lasrosasiot
fi

# Java
if [ -z "$JAVA_HOME" ];
then
  export JAVA_HOME="/devel/java/jdk-17"
fi

if [ ! -d "$JAVA_HOME" ]; then
  echo "Invalid JAVA_HOME variable: folder $JAVA_HOME does not exist."
fi

PATH="$PATH:$JAVA_HOME/bin"

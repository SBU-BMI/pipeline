#!/bin/bash

# Error trapping
PROGNAME=$(basename "$0")
error_exit() {
   echo "${PROGNAME}: ${1:-"Error"}" 1>&2
   echo "Line $2"
   exit 1
}

setup_src_dir()
{
  # Set up source directory
  dir="$1"
  if [[ ! -d "$dir" ]]; then
    echo "Making directory $dir"
    mkdir -p "$dir" || error_exit "Could not create directory $dir" $LINENO
  fi

}

setup_bin()
{
  # Set up the executable
  dir="$1"
  script="$2"
  link="$3"

  # Copy file
  cp "$script" "$dir" || error_exit "Could not copy file $script to $dir" $LINENO

  # Check symlink
  if [ ! -f "$link" ]; then
    echo "Creating symlink $link"
    ln -s "$dir/$script" "$link" || error_exit "Could not create symlink $link" $LINENO
  else
    # Re-do it just in case we moved the script location
    rm "$link" || error_exit "Could not remove symlink $link" $LINENO
    ln -s "$dir/$script" "$link" || error_exit "Could not create symlink $link" $LINENO
  fi

}

if [[ $# -lt 3 ]] ; then
  local="/usr/local"
  directory="$local/BMI/pipeline"
  script="segment_wsi.sh"
  link="$local/local/bin/segmentwsi"

  echo
  echo 'usage: ${PROGNAME} directory script symlink_path]'
  echo
  echo 'defaults:'
  echo "script directory" "$directory"
  echo "script filename" "$script"
  echo "symlink path" "$link"
  echo

  setup_src_dir "$directory"
  setup_bin "$directory" "$script" "$link"

else

  # Remember to put quotes around the file paths when calling this script.
  # ./install.sh "/path/to/source/dir" "segment_wsi.sh" "/path/to/bin/symlink"

  directory="$1"
  script="$2"
  link="$3"

  echo
  echo "script directory" "$directory"
  echo "script filename" "$script"
  echo "symlink path" "$link"
  echo

  setup_src_dir "$directory"
  setup_bin "$directory" "$script" "$link"

fi

echo "Done."

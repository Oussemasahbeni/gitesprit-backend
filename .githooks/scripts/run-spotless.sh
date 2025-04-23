#!/bin/sh
# Run Spotless check
echo "Running Spotless check..."
mvn spotless:check

# Check if Spotless found any issues
# Check if Spotless found any issues
if [ $? -ne 0 ]; then
    echo "Spotless check failed! Applying fixes..."
    mvn spotless:apply

    # Get the list of files modified by Spotless
    modified_files=$(git diff --name-only)

    if [ -z "$modified_files" ]; then
        echo "No files were modified by Spotless."
        exit 0  # Proceed with the commit if no files were modified
    fi

    echo "Re-staging modified files..."
    # Add only the modified files to the current stage
    git add $modified_files

    # Append the Spotless commit message to the current commit message
    current_commit_message=$(git log -1 --pretty=%B | head -n 1)  # Get the current commit message
    new_commit_message="${current_commit_message}"$'\n\n'"chore(XXXXX): Spotless code formatting"

    # Update the commit message
    echo "$new_commit_message" > .git/COMMIT_EDITMSG

    echo "Formatted files have been added, and commit message updated."
fi

echo "Spotless check passed! Proceeding with commit."
exit 0
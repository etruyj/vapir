# VAPIr (Vail API)

## Description
VAPIr is a command line script for both linux and Windows (.bat) environments to allow automated administration a Spectra Logic Vail storage solution. This is a work in progress that is being expanded as needed. The initial goal of this program is to allow the filtering of users by their account and/or if the account is activated in the Vail Sphere. This can also be used to see what accounts buckets belong to and filter buckets by account. If any features are desired, please log them in the Issues as enhancement requests.

Vail does not store a human readable name for the account. This code substitutes the username specified when attaching the account to the Vail Sphere as the name of the account. For the AWS account housing the Sphere, "Sphere" will be listed as the account name.

This code uses Google's Gson for parsing JSON API responses and requires JDK 14 or higher to run.

## Using
vapir is a command line tool. After decompressing the tar or zip file, nagivate to the bin directory to find the vapir shell or batch script. Windows users will use the batch script (.bat). Linux/Mac OS users will use the shell script to execute commands.

### Samples
The following samples demonstrate a few of the commands for vapir. Capitalized words represent variable values that should be edited to reflect the execution environment. For example, IP_ADDRESS must be replaced with the IP address of the Spectra Vail Sphere (management server), such as 10.10.10.15. 

Some of the flags can be abbreviated to their initial letter. This can be seen in the below examples where both --command and -c work, --endpoint and -e work, and --username and -u work for parsing the variable.

#### Get a list of all accounts (Linux/Mac OS)
`./vapir --ignore-ssl --endpoint IP_ADDRESS --username USERNAME --password PASSWORD --command list-accounts`

#### List All Active (Windows)
`./vapir.bat --ignore-ssl --endpoint IP_ADDRESS --username USERNAME --password PASSWORD -c list-users --active-only`

#### List All Users Belonging To Account 0123456789 in CSV Format(Linux/Mac OS)
`./vapir --ignore-ssl -e IP_ADDRESS -u USERNAME -p PASSWORD -c list-users --account 0123456789 --output-format csv`

#### List All Buckets(Windows)
`./vapir.bat --ignore-ssl -e IP_ADDRESS -u USERNAME -p PASSWORD -c list-buckets --account all`

### Options
--account&emsp;&emsp;Filter list by owning account name or account id. Use all to list all items in response.
--active-only&emsp;Filter the user list to only display active users.
--command(-c)&emsp;The command to be executed. Commands can be listed with -c help, -c help-basic, or -c help-advanced  
--ignore-ssl&emsp;&emsp;Ignore the SSL certificate returned by the server.
--endpoint(-e)&emsp;&ensp;IP Address of the Stack  
--output-format&emsp;&ensp;The format in which ouput will be displayed. Accepts csv, shell, table, and xml. Defaults to table.
--password(-p)&emsp;Password  
--username(-u)&emsp;&ensp;Username  

### Commands
--configure-sphere&emsp;Loads a JSON configuration file to configure a sphere.
--fetch-config&emsp;&emsp;Saves accounts, groups, storage locations, lifecycles, and bucket info in JSON format to --file
--list-accounts&emsp;List all accounts paired with the Vail system.
--list-buckets&emsp;List buckets. Use --account [ all | ACCOUNT_NAME | ACCOUNT_ID ] to filter results.
--list-users&emsp;&ensp;List users. Use --account [ all | ACCOUNT_NAME | ACCOUNT_ID ] and/or --active-only to filter results.

## Errors
vapir stores logs in the log/ sub-directory for the program. The name of the file is vail_api.log

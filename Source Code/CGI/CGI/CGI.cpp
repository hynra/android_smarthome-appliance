// CGI.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <iostream>

using namespace std;

int main()
{
	cout << "Content-type:text/html\r\n\r\n"
		<< "<html>\n"
		<< "<head>\n"
		<< "<title>Program CGI</title>\n"
		<< "</head>\n"
		<< "<h2></h2>"
		<< "<body>\n"
		<< "<form>\n"
		<< "<input type = 'button' value='ON'>\n"
		<< "<input type = 'button' value='OFF'>\n"
		<< "</form>\n"
		<< "</body>\n"
		<< "</html>\n";
    return 0;
}


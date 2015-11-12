
#define STRICT
#include "stdafx.h"
#include <tchar.h>
#include <Windows.h>
#include "Serial.h"
#include <iostream>
#include <stdlib.h>

using namespace std;

int main(HINSTANCE, HINSTANCE, LPTSTR, int)
{
	char *data;
	char status;
	CSerial serial;

	serial.Open(_T("COM8"));
	serial.Setup(CSerial::EBaud9600, CSerial::EData8, CSerial::EParNone, CSerial::EStop1);
	serial.SetupHandshaking(CSerial::EHandshakeOff);

	//inform 
	cout << "Content-type:text/html\r\n\r\n"
		<< "<html>\n"
		<< "<head>\n"
		<< "<title>Program CGI</title>\n"
		<< "</head>\n"
		<< "<body>\n";

	data = getenv("QUERY_STRING");
	if (data != NULL) {
		sscanf_s(data, "%c", &status);
		switch (status)
		{
		case '1':
			serial.Write("1");
			cout << "<h2>Status: ON</h2>";
			break;
		case '0':
			serial.Write("0");
			cout << "<h2>Status: OFF</h2>";
			break;
		default:
			break;
		}
	}

	serial.Close();

	cout << "</body>\n"
		<< "</html>\n";

    return 0;
}


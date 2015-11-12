// TurnOn.cpp : Defines the entry point for the console application.
//

#define STRICT
#include "stdafx.h"
#include <tchar.h>
#include <Windows.h>
#include "Serial.h"
#include <iostream>

using namespace std;

int main(HINSTANCE, HINSTANCE, LPTSTR, int)
{
	CSerial serial;

	serial.Open(_T("COM8"));
	serial.Setup(CSerial::EBaud9600, CSerial::EData8, CSerial::EParNone, CSerial::EStop1);
	serial.SetupHandshaking(CSerial::EHandshakeOff);

	serial.Write("3");

	serial.Close();

	//inform 
	cout << "Content-type:text/html\r\n\r\n"
		<< "<html>\n"
		<< "<head>\n"
		<< "<title>Program CGI</title>\n"
		<< "</head>\n"
		<< "<body>\n"
		<< "<h2>Status: OFF</h2>"
		<< "</body>\n"
		<< "</html>\n";

	return 0;
}


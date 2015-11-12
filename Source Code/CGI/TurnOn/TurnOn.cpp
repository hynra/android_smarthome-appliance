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

	serial.Write("1");

	serial.Close();

	//inform return json
	cout << "Content-type:application/json\r\n\r\n"
		<< "{\"status\":\"on\"}\n";

    return 0;
}


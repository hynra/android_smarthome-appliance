int incomingByte = 0;   // for incoming serial data
int pin = 12;
bool send = false;
bool blink = false;
bool alrmOn = false;
bool sendAlrmOff = false;

void setup() {
  Serial.begin(9600, SERIAL_8N1);  
  pinMode(pin, OUTPUT);
}

void loop() {
  // send data only when you receive data:
        if (Serial.available() > 0) {
                // read the incoming byte:
                incomingByte = Serial.read();
                switch(incomingByte){
                  case 52: //stop firing alarmoff values
                    sendAlrmOff=false;
                    break;
                  case 51: // turn alarmoff
                    alrmOn = false;
                    sendAlrmOff=true;
                    blink = false;
                    break;
                  case 50: //set alarm
                    blink = true;
                    alrmOn = true;
                    break;
                  case 49: // turn on light
                   digitalWrite(pin, HIGH);
                    break;
                  case 48: //turn off light
                    digitalWrite(pin, LOW);
                    blink = false;
                    break;
                  case 101: // capture image
                    send = true;
                    break;
                  case 100:
                    send = false;
                    break;
                  default:
                     break;
                }
                
                // say what you got:
                //Serial.print("I received: ");
               //Serial.println(incomingByte);
        }
        if(send){
           sendAndroidValues();
           delay(2000);
        }
        if(blink){
          digitalWrite(pin, HIGH);   // turn the LED on (HIGH is the voltage level)
          delay(300);              // wait for a second
          digitalWrite(pin, LOW);    // turn the LED off by making the voltage LOW
          delay(200); 
        }
        if(alrmOn){
          sendAlarmValues();
          //delay(2000); 
        }
        if(sendAlrmOff){
          sendAlarmOff();
          //delay(2000); 
        }
}
        
void sendAndroidValues()
 {
  //puts # before the values so our app knows what to do with the data
   Serial.print('#');
   Serial.print("hello");
   Serial.print('+');
   Serial.print('~'); //used as an end of transmission character - used in app for string length
   Serial.println();
   delay(10);        //added a delay to eliminate missed transmissions
}

void sendAlarmValues()
 {
  //puts # before the values so our app knows what to do with the data
   Serial.print('#');
   Serial.print("r");
   Serial.print('+');
   Serial.print('~'); //used as an end of transmission character - used in app for string length
   Serial.println();
   delay(10);        //added a delay to eliminate missed transmissions
}

void sendAlarmOff()
 {
  //puts # before the values so our app knows what to do with the data
   Serial.print('#');
   Serial.print("3");
   Serial.print('+');
   Serial.print('~'); //used as an end of transmission character - used in app for string length
   Serial.println();
   delay(10);        //added a delay to eliminate missed transmissions
}

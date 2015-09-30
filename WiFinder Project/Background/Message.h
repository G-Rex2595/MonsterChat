/* Project:  WiFinder
 * File:  Message.h
 * Author:  Andrew Sytsma
 * 
 * Message.h is used to abstract the methods that
 * are coded in Message.cpp.
 */

#ifndef Message_H
#define Message_H

#include <string>
#include <time.h>

using std::string;

class Message
{
public:
	Message(string username, string msg, string id, time_t time);
	virtual string getName();
	virtual string getMessage();
	virtual time_t getTime();
	virtual string getID();

private:
	string username;
	string msg;
	string id;
	time_t time;
};

#endif
/* Project:  WiFinder
 * File:  Message.h
 * Author:  Andrew Sytsma
 * 
 * [INSERT DESCRIPTION]
 */

#ifndef Message_H
#define Message_H

#include <string>
#include <time.h>

using std::string;

class Message
{
public:
	Message(string username, string msg, string id);
	virtual string getName();
	virtual string getMessage();
	virtual time_t getTime();
	virtual string getID();
	virtual void setTime(time_t time);
};

#endif
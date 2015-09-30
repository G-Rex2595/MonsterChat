/* Project:  WiFinder
 * File:  Message.cpp
 * Author:  Andrew Sytsma
 *
 * Message.cpp holds the information needed for a message.  The data
 * is set in the constructor and is retrieved via the helper methods.
 */

#include "Message.h"

/* Sets the class variables.
 *
 * @param username Holds the sender's unsername.
 * @param msg Holds the message received.
 * @param id Holds the id of the sender.
 * @param time Holds the time received.
 */

Message::Message(string username, string msg, string id, time_t time)
{
	Message::username = username;
	Message::msg = msg;
	Message::id = id;
	Message::time = time;
}	//end of Message constructor

/* Returns the sender's username.
 * 
 * @return Returns the sender's username.
 */

string Message::getName()
{
	return Message::username;
}	//end of getName method

/* Returns the message received.
 *
 * @return Returns the message received.
 */

string Message::getMessage()
{
	return Message::msg;
}	//end of getMessage method

/* Returns the time received.
 *
 * @return Returns the time received.
 */

time_t Message::getTime()
{
	return Message::time;
}	//end of getTime method

/* Returns the id of the sender.
 *
 * @return Returns the id of the sender.
 */

string Message::getID()
{
	return Message::id;
}	//end of getID method
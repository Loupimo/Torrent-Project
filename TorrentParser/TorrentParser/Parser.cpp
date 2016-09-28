#include "Parser.h"
#include "Dictionnaries.h"
#include "Global.h"

Parser::Parser()
{

}

Parser::Parser(string torrentFileName)
{
	this->torrentFile.open(torrentFileName, ios::in | ios::binary);
	if (this->torrentFile.is_open())
	{
		this->offset = 0;
		readFile();
	}
	cout << "Fin Parser" << endl;
}

Parser::Parser(Parser& parser)
{
	this->fileSize = parser.fileSize;
	this->offset = parser.offset;
	this->datas = parser.datas;
}

Parser::~Parser()
{
}


void Parser::readFile()
{ // Read all the file

	cout << "Read all file" << endl << endl;
	char * mem = new char[1]; // Temp variable
	
	this->torrentFile.seekg(0, this->torrentFile.end);
	int end = this->torrentFile.tellg();
	this->torrentFile.seekg(0, this->torrentFile.beg);

	this->fileSize = end; // Size of the file

	this->datas = new char [end];
	cout << end << endl << endl;

	for (int i = 0; i < end ; i ++)
	{ // Reads and stores all the datas in the datas array
		this->torrentFile.read(mem, 1);
		if (mem[0] >= '\x00' && mem[0] <= '\x0F')
		{
			mem[0] = '\x10';
			//cout << "coucou" << endl;
		}
		this->datas[i] = mem[0];
	}

	this->torrentFile.close();
	cout << this->datas << endl;
	parseData();
}

void Parser::parseData()
{ // Parse the datas
	cout << "Parsedata from parser" << endl;
	for (offset = 0; offset < this->fileSize; offset++)
	{
		//cout << endl << offset;
		nextDataType(this->datas[offset]);

		//cout << ", " << this->datas[this->offset] << endl;
		//cout << offset << endl;
	}
	for (int i = 0; i < torrentParsed.size(); i++)
	{
		cout << "Parsed Info: " << i << endl;
		torrentParsed.at(i)->printGlobal();
	}
}

void Parser::nextDataType(char type)
{ // Determines the type of the next encountred data
	if (type == 'd')
	{
		cout << "new dico from parser: " << this->offset << ", " << this->datas[this->offset] << endl;
		this->offset++;
		this->torrentParsed.push_back(new Global (new Dictionnaries(*this)));
	}
	else if (type == 'l')
	{
		cout << "new list from parser: " << this->offset << ", " << this->datas[this->offset] << endl;
		this->torrentParsed.push_back(new Global(new Listes(*this)));
	}
	else if (type <= '\x39' && type >= '\x30')
	{ // If it's a number we look for delimiters (":") to get the length of the next string
	  //cout << "get length string from dico: " << type << endl;
		string s_lengthOfNextString;
		s_lengthOfNextString += type;

		this->offset++;
		for (this->offset = this->offset; this->offset < this->fileSize; this->offset++)
		{
			if (this->datas[this->offset] == ':')
			{ // Delimiters found
				this->offset++;
				break;
			}
			else s_lengthOfNextString += this->datas[this->offset]; // Adds the current number to the end of the string
		}

		//cout << s_lengthOfNextString << endl;
		int i_lengthOfNextString = stoi(s_lengthOfNextString); // Converts the string to int
		getString(i_lengthOfNextString);
	}

	else if (type == '-' || type == 'i')
	{ // It's an integer (which constitutes a definition)
	  //cout << "get an integer from dico: " << type << endl;
		string s_integer;
		this->offset++;

		if (type == '-')
		{
			this->offset++; // After the "-" there's always an "i" which is the begin tag of an integer
			s_integer += type;
		}

		for (this->offset = this->offset; this->offset < this->fileSize; this->offset++)
		{
			if (this->datas[this->offset] == 'e')
			{ // Delimiters found
				break;
			}
			else s_integer += this->datas[this->offset]; // Adds the current number to the end of the string
		}

		//cout << s_integer << endl;
		this->torrentParsed.push_back(new Global(s_integer));
	}
}



void Parser::getString(int lengthOfString)
{ // Gets the string corresponding to the length

	string info;
	for (int i = 0; i < lengthOfString; i++)
	{
		info += this->datas[this->offset];
		this->offset++;
	}
	//cout << "string: " << info << ", length: " << lengthOfString << endl;

	this->offset--; // Sets the cursor to the previous character for a proper analysis (to not interfer with the offset++ of the parseData function))
	this->torrentParsed.push_back(new Global(info));
}
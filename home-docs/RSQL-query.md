###Tutorial of creating selected query from database
***
The language RSQL is  used to get data from the database .
RSQL is a query language for RESTful APIs. It uses an URI-friendly syntax, there are no unsafe characters, so URL encoding is not required. It supports some logical and comparison operators and can be easily extended by custom operators.
RSQL expression is composed of one or more comparisons, related to each other with logical operators
Comparison is composed of a selector, an operator and an argument.
Selector identifies a field (or attribute, element, …) of the resource representation to filter by. It can be any non empty Unicode string that doesn’t contain reserved characters (see below) or a white space. 
Argument can be a single value, or multiple values in parenthesis separated by comma.Arguments must be enclosed in single or double quotes.
RSQL allows you to dispose of a large number of methods in the Jpa repositories forming by predicate.
You can use only one method in any Jpa  *findAll()* transfer to it predicate as argument.
Using RSQL you  can get filtered and sorted data from database. In addition, you can use search predicate to get data result set. 
 ***
 ***RSQL Syntax Reference for filtering data***
 - filter = "id=bt=(2,4)";// id>=2 && id<=4 //between
 - filter = "id=nb=(2,4)";// id<2 || id>4 //not between
 - filter = "company.code=like=em"; //like %em%
 - filter = "company.code=ilike=EM"; //ignore case like %EM%
 - filter = "company.code=icase=EM"; //ignore case equal EM
 - filter = "company.code=notlike=em"; //not like %em%
 - filter = "company.code=inotlike=EM"; //ignore case not like %EM%
 - filter = "company.code=ke=e*m"; //like %e*m%
 - filter = "company.code=ik=E*M"; //ignore case like %E*M%
 - filter = "company.code=nk=e*m"; //not like %e*m%
 - filter = "company.code=ni=E*M"; //ignore case not like %E*M%
 - filter = "company.code=ic=E^^M"; //ignore case equal E^^M
 - filter = "company.code==demo"; //equal
 - filter = "company.code=='demo'"; //equal
 - filter = "company.code==''"; //equal to empty string
 - filter = "company.code==dem*"; //like dem%
 - filter = "company.code==*emo"; //like %emo
 - filter = "company.code==*em*"; //like %em%
 - filter = "company.code==^EM"; //ignore case equal EM
 - filter = "company.code==^*EM*"; //ignore case like %EM%
 - filter = "company.code=='^*EM*'"; //ignore case like %EM%
 - filter = "company.code!=demo"; //not equal
 - filter = "company.code=in=(*)"; //equal to *
 - filter = "company.code=in=(^)"; //equal to ^
 - filter = "company.code=in=(demo,real)"; //in
 - filter = "company.code=out=(demo,real)"; //not in
 - filter = "company.id=gt=100"; //greater than
 - filter = "company.id=lt=100"; //less than
 - filter = "company.id=ge=100"; //greater than or equal
 - filter = "company.id=le=100"; //less than or equal
 - filter = "company.id>100"; //greater than
 - filter = "company.id<100"; //less than
 - filter = "company.id>=100"; //greater than or equal
 - filter = "company.id<=100"; //less than or equal
 - filter = "company.code=isnull=''"; //is null
 - filter = "company.code=null=''"; //is null
 - filter = "company.code=na=''"; //is null
 - filter = "company.code=nn=''"; //is not null
 - filter = "company.code=notnull=''"; //is not null
 - filter = "company.code=isnotnull=''"; //is not null
 - filter = "company.code=='demo';company.id>100"; //and
 - filter = "company.code=='demo' and company.id>100"; //and
 - filter = "company.code=='demo',company.id>100"; //or
 - filter = "company.code=='demo' or company.id>100"; //or
 ***
 ***RSQL Syntax Reference for sorting data***
 - sort = "id,asc"; // order by id asc
 - sort = "id,asc;company.id,desc"; // order by id asc, company.id desc
 ***
The query-string parameters are used to get data from Users table 
- pageNumber (integer) is parameter for data paging   Default: 1>=1
- pageSize (integer)   is parameter for paging size   Default: 5 >=0 and <= 10
- sort (string) is parameters for sorting data       Default: id,asc
- filter (string) is parameters for filtering data
- id (string) is parameters for searching data by ID
- email (string) is parameters for searching data by email
- firstName (string) is parameters for searching data by firstName
- lastName (string) is parameters for searching data by lastName
- contact (string) is parameters for searching data by contact
***
The query-string parameters are used to get data from News table 
- pageNumber (integer) is parameter for data paging   Default: 1>=1
- pageSize (integer)   is parameter for paging size   Default: 5 >=0 and <= 10
- sort (string) is parameters for sorting data       Default: id,asc
- filter (string) is parameters for filtering data
- id (string) is parameters for searching data by ID
- title (string) is parameters for searching data by title
- text (string) is parameters for searching data by text
- source (string) is parameters for searching data by source

  
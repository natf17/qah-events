# A Custom Application for Managing AH Events

This Spring Boot application is a REST API that manages creating, updating, deleting, and retrieving events in various languages. You can manage translations and events at `/translations` and `/event`, respectively. For operations on a list of events, such as getting or deleting a range of events, use `/events`. You could also load a translated "view" of an event in a certain language at `/event/translated` For a range of translated events, use `/events/translated`.

What follows is a basic rundown of the application's capabilities.


### `GET /events` 

GET a list of complete events that meets request parameters

Request parameters:
- `eventLang`
- `eventType`
- `after`
- `before`

**Ex:** `GET /events?eventLang=en&eventType=REG&after=2018-07-01&before=2018-08-01` 

... returns: 
```
{
	events : [
		{
			id: 34,
			defaultTranslationId: 98,
			eventType: "REG",
   			defaultDataLang: "en",
   			eventStartDate: "2018-07-06",
   			eventEndDate: "2018-07-08",
   			eventTitle : "Be courageous",
   			eventLanguage : "sp",
   			dataLanguage : "en",
   			comments : "Caution",
   			eventTranslations : [
   					{
   						id: 99
   						eventTitle: "Sea valiente",
   						eventLanguage: "esp",
   						dataLanguage:"esp",
   						comments: "Cuidado"
   					},
  					{
  						id: 100
  						eventTitle: "Soix courageux",
   						eventLanguage: "esp",
   						dataLanguage:"fr",
   						comments: "Mise en garde"
  					}
	   
   			]
   		},
   
   		{ ... }
 	   
   	]
   
}  
	  
```
... and `200 OK`.

**Note:** any event that is ongoing between the `before` and `after` parameters will be returned as a matching event.


### `POST /events`

POST a list of complete events

**Ex:** `POST /events` 
... with request body:

```
{
	events : [
		{
			eventType : "REG",
   			defaultDataLang : "en",
   			eventStartDate : "2018-07-06",
   			eventEndDate : "2018-07-08",
   			eventTitle : "Be courageous",
   			eventLanguage : "sp",
   			dataLanguage : "en",
   			comments : "Caution",
   			translations : [
   					{
   						eventTitle: "Sea valiente",
   						eventLanguage: "esp",
   						dataLanguage:"sp",
   						comments: "Cuidado"
   					},
  					{
  						eventTitle: "Soix courageux",
   						eventLanguage: "esp",
   						dataLanguage:"fr",
   						comments: "Mise en garde"
  					}
	   
   			]
   		},
   
   		{ ... }
 	   
   	]
   
}  
```
... will send the events for processing and return `201 Created` if successful.

**Optional fields:**
- `eventEndDate`: if not provided, the `eventStartDate` will be used as the end date
- `defaultDataLang`: if not provided, the `dataLanguage` of the event will be used as the default


### `DELETE /events`

DELETE all events (along with translations) that match the request parameters

Request parameters:
- `eventLang`
- `eventType`
- `after`
- `before`

**Ex:** `DELETE /events?eventLang=en&eventType=REG&after=2018-07-01&before=2018-08-08`

- Deletes all `REG` events that are in English and are between July 1, 2018 and August 8, 2018
- Returns `204 NO CONTENT` if delete is successful.

**Note:** any event that is ongoing between the `before` and `after` parameters will match and therefore be deleted.


### `GET /event/{id}`

GET the complete event with the given `id`

**Ex:** `GET /event/131`

... returns:
```
 {
 	id : 131,
 	defaultTranslationId: 145,
  	eventType : "REG",
   	eventStartDate : "2018-07-06",
   	eventEndDate : "2018-07-08",
   	eventTitle: "Be courageous",
   	eventLanguage: "sp",
	defaultDataLang : "en",
	dataLanguage: "en",
	comment: "Caution",
 	eventTranslations:[
 	   		{
 	   			id: 5465,
 	   			eventTitle: "Sea valiente",
 	   			eventLanguage: "esp",
 	   			dataLanguage:"sp",
 	   			comments: "Cuidado"
 	   		},
 	   		{
 	   			id: 8765,
 	   			eventTitle: "Soix courageux",
 	   			eventLanguage: "esp",
 	   			dataLanguage:"fr",
 	   			comments: "Mise en garde"
 	  		}
 	   
 	]
 	   
 }
 ``` 
... and `200 OK`.


### `POST /event`

POST a single complete event

**Ex:** `POST /event` 
... with request body:
```
{
  	eventType : "REG",
   	eventStartDate : "2018-07-06",
   	eventEndDate : "2018-07-08",
   	eventTitle: "Be courageous",
   	eventLanguage: "sp",
	defaultDataLang : "en",
	dataLanguage: "en",
	comment: "Caution",
 	eventTranslations:[
 	   		{
 	   			eventTitle: "Sea valiente",
 	   			eventLanguage: "esp",
 	   			dataLanguage:"sp",
 	   			comments: "Cuidado"
 	   		},
 	   		{
 	   			eventTitle: "Soix courageux",
 	   			eventLanguage: "esp",
 	   			dataLanguage:"fr",
 	   			comments: "Mise en garde"
 	  		}
 	   
 	]
 	   
 }
```
... will send the event for processing and return `201 Created` if successful.

**Optional fields:**
- `eventEndDate`: if not provided, the `eventStartDate` will be used as the end date
- `defaultDataLang`: if not provided, the `dataLanguage` of the event will be used as the default


### `PUT /event/{id}`

PUT a single complete event

**Ex:** `PUT /event/24` 
... with request body:
```
 {
 	id: 24,
	eventType : "REG",
   	eventStartDate : "2018-07-06",
   	eventEndDate : "2018-07-08",
   	eventTitle: "Be courageous",
   	eventLanguage: "sp",
	defaultDataLang : "en",
	dataLanguage: "en",
	comment: "Caution",
 	eventTranslations : [
 	   		{
 	   			eventTitle: "Sea valiente",
 	   			eventLanguage: "esp",
 	   			dataLanguage:"sp",
 	   			comments: "Cuidado"
 	   		},
 	   		{
 	   			eventTitle: "Soix courageux",
 	   			eventLanguage: "esp",
 	   			dataLanguage:"fr",
 	   			comments: "Mise en garde"
 	  		}
	]
 	   
 }
 ```  
... updates the event with the given `id`, completely replacing the entity in the database with the request body. This updated event will not be checked to see if it has become a duplicate of an existing event. `204 No Content` is returned upon success.


### `DELETE /event/{id}`

DELETE the complete event with the given `id`

**Ex:** `DELETE /event/43`

- Deletes the event with `id` 43
- Returns `204 No Content` if delete is successful.


### `GET /events/translated` 

GET a list of translated events that meets the request parameters

Request parameters:
- `eventLang`
- `eventType`
- `after`
- `before`
- `lang`

**Ex:** `GET /events/translated?eventLang=en&lang=sp&eventType=REG&after=2018-07-01&before=2018-08-18` 

... returns all English `REG` events between July 1 and August 18, 2018 that have a spanish translation: 
```
{
 	events : [
  		{
  			"id": 6543,
  			"currentTranslationId": 567,
  			"eventLanguage": "ing",
 			"dataLanguage": "sp",
  			"eventTitle": "Sea valiente!",
  			"comments": "nada",
  			"eventType": "REG",
 			"eventStartDate": "2018-07-21",
 			"eventEndDate": "2018-07-21",
  			"defaultDataLang": "en"
	 	},
	 	{
	 		"id": 434,
	 		"currentTranslationId": 587,
	 		"eventLanguage": "ing",
	 		"dataLanguage": "sp",
	 		"eventTitle": "Sea valiente!",
	 		"comments": "none",
	 		"eventType": "REG",
	 		"eventStartDate": "2018-07-22",
 			"eventEndDate": "2018-07-23",
  			"defaultDataLang": "en"
	 	}
  	]
}
	  
```
... and `200 OK`.

**Note:** any event that is ongoing between the `before` and `after` parameters will be returned as a matching event.
**Note:** if `lang` is not provided, the translations are returned in English.
**Note:** `id` is the `id` of the event and does not reflect the language of event information.


### `GET /event/translated/{id}` 

GET a the event with the provided `id` in the requested language

Request parameters:
- `lang`

**Ex:** `GET /event/translated/6543?lang=sp` 

... returns:
```
{
  	"id": 6543,
  	"currentTranslationId": 567,
  	"eventLanguage": "ing",
 	"dataLanguage": "sp",
  	"eventTitle": "Sea valiente!",
  	"comments": "nada",
  	"eventType": "REG",
 	"eventStartDate": "2018-07-21",
 	"eventEndDate": "2018-07-21",
  	"defaultDataLang": "en"
}
	  
```
... and `200 OK`.

**Note:** if `lang` is not provided, the translation is returned in English.
**Note:** `id` is the `id` of the event and does not reflect the language of event information.


### `GET /translations/{id}`

GET the translation with the given id

**Ex:** `GET /translations/58`

... returns:
```
 	{
	 	id:58,
 	   	eventTitle: "Sea valiente",
 	   	eventLanguage: "ing",
 	   	dataLanguage:"sp",
 	   	comments: "Cuidado"
 	}
``` 
... and `200 OK`.


### `POST /translations/event/{id}`

POST a new translation to the event with the given `id`

**Ex:** `POST /translations/event/78` 
... with request body:
```
 	{
 	  	eventTitle: "Sea valiente",
 	  	eventLanguage: "ing",
 	  	dataLanguage:"sp",
 	  	comments: "Cuidado"
 	}
 ```
...will send the translation for processing. It will add a new translation to the event with `id` 78 and return `201 Created` if successful.


### `PUT /translations/{id}`

PUT a translation

**Ex:** `PUT /translations/57` 
... with request body:
```
 	{
	 	id:57,
 	  	eventTitle: "Sea valiente",
 	   	eventLanguage: "ing",
 	   	dataLanguage:"sp",
 	   	comments: "Cuidado"
 	}
```
... updates the translation with `id` 57 by completely overwriting the previous translation and returns `204 No Content` if successful.

**Note:** An event's default translation `dataLanguage` cannot be changed.


### `DELETE /translations/{id}`

DELETE the EventTranslation with the given id

**Ex:** `DELETE /translations/53`

- Deletes the translation with `id` 53
- Returns `204 No Content` if successful.


## Future enhancements
	1. ?? If an event is requested in a language that doesn't exist, it should be returned in its default
	2. Secure endpoints
	3. Allow for changing allowable languages




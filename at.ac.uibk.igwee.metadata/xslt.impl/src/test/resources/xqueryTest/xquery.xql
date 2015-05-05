xquery version "1.0";
declare namespace tei="http://www.tei-c.org/ns/1.0";

for $tei in collection("./xml/")
	let $title := $tei//tei:title[1]
	where $title/tei:date[contains(@when, '14')]
	return <result> {$title/string()} </result>
function converter(json) {
    var text = json.text;
    print(typeof json)
    var name = json.name;
    var age = json.age;
    print(typeof text)
    print(typeof age)
    return text + "_" + name + "_" + age;
}
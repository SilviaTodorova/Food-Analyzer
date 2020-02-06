# Food Analyzer Server

[FoodData Central API Guide](https://fdc.nal.usda.gov/api-guide.html)
[FoodData Search](https://fdc.nal.usda.gov/fdc-app.html#/)
[UPC codes and images](https://www.upcitemdb.com/upc/milka)
[upc chia example](https://www.upcitemdb.com/upc/856220004034)

# Food Analyzer Client

### get-food <food_name>
извежда информацията за даден хранителен продукт. Ако сървърът върне множество продукти с даденото име, се извежда информация за всеки от тях. Ако пък липсва информация за продукта, се извежда подходящо съобщение.

### get-food-report <food_fdcId>
по даден уникален идентификатор на продукт (fdcId) извежда име на продукта, съставки (ingedients), енергийна стойност (калории), съдържание на белтъчини, мазнини, въглехидрати и фибри.

### get-food-by-barcode --code=<gtinUpc_code>|--img=<barcode_image_file>
извежда информация за продукт по неговия баркод, ако такава е налична в кеша на сървъра (обърнете внимание, че REST API-то не поддържа търсене на продукт по gtinUpc код или баркод изображение). Задължително е да подадем един от двата параметъра: или code, или файл, съдържащ баркод изображение (като пълен път и име на файла на локалната файлова система на клиента). Ако са указани и двата параметъра, img параметърът се игнорира.

## Пример за валидни входни данни

<pre>
    <code>
    get-food beef noodle soup
    get-food-report 415269
    get-food-by-barcode --img=D:\Photos\BarcodeImage.jpg --code=009800146130
    </code>
</pre>
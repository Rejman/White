package Utils;

import Dao.*;
import Models.*;
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.text.ParseException;
import java.util.Date;

public class DBManager {
    public static final String TAGS = "CREATE TABLE IF NOT EXISTS tags (\n"
            + "     id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "     name TEXT NOT NULL UNIQUE\n"
            + ");";
    public static final String CATEGORIES = "CREATE TABLE IF NOT EXISTS categories (\n"
            + "     id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "     name TEXT NOT NULL UNIQUE,\n"
            + "     color TEXT NOT NULL\n"
            + ");";
    public static final String EXPENSES = "CREATE TABLE IF NOT EXISTS expenses (\n"
            + "     id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "     name_id INTEGER NOT NULL,\n"
            + "     unit_id INTEGER NOT NULL,\n"
            + "     FOREIGN KEY(name_id) REFERENCES names(id),\n"
            + "     FOREIGN KEY(unit_id) REFERENCES units(id)\n"
            + ");";
    public static final String SOURCES = "CREATE TABLE IF NOT EXISTS sources (\n"
            + "     id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "     name TEXT NOT NULL UNIQUE,\n"
            + "     description TEXT,\n"
            + "     color TEXT NOT NULL\n"
            + ");";
    public static final String TRANSACTIONS = "CREATE TABLE IF NOT EXISTS transactions (\n"
            + "     id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "     date TEXT NOT NULL,\n"
            + "     source_id INTEGER NOT NULL,\n"
            + "     expense_id INTEGER NOT NULL,\n"
            + "     number REAL NOT NULL DEFAULT 0,\n"
            + "     unit_price REAL NOT NULL DEFAULT 0,\n"
            + "     tag_id INTEGER NULL,\n"
            + "     FOREIGN KEY(expense_id) REFERENCES expenses(id),\n"
            + "     FOREIGN KEY(source_id) REFERENCES sources(id),\n"
            + "     FOREIGN KEY(tag_id) REFERENCES tags(id)\n"
            + ");";
    public static final String NAMES = "CREATE TABLE IF NOT EXISTS names (\n"
            + "     id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "     name TEXT NOT NULL UNIQUE,\n"
            + "     category_id INTEGER NOT NULL,\n"
            + "     FOREIGN KEY(category_id) REFERENCES categories(id)\n"
            + ");";
    public static final String UNITS = "CREATE TABLE IF NOT EXISTS units (\n"
            + "     id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "     name TEXT NOT NULL UNIQUE,\n"
            + "     shortcut TEXT UNIQUE,\n"
            + "     real_number INTEGER NOT NULL DEFAULT 0\n"
            + ");";
    public static final String BASIC_DATA_VIEW = "CREATE VIEW IF NOT EXISTS basic_data AS\n"
            + "     SELECT\n"
            + "     e.id as expense_id,\n"
            + "     c.id as category_id,\n"
            + "     c.name as category_name,\n"
            + "     c.color as category_color,\n"
            + "     n.id as name_id,\n"
            + "     n.name as name,\n"
            + "     u.id as unit_id,\n"
            + "     u.name as unit_name,\n"
            + "     u.shortcut,\n"
            + "     u.real_number\n"
            + "     FROM categories c\n"
            + "     LEFT JOIN names n ON n.category_id=c.id\n"
            + "     LEFT JOIN expenses e ON e.name_id=n.id\n"
            + "     LEFT JOIN units u ON u.id=e.unit_id;";
    public static final String TRANSACTIONS_VIEW = "CREATE VIEW IF NOT EXISTS transactions_with_total AS\n"
            + "     SELECT\n"
            + "     t.id,\n"
            + "     t.date,\n"
            + "     s.name AS source,\n"
            + "     n.name AS expense,\n"
            + "     t.number,\n"
            + "     u.shortcut AS unit,\n"
            + "     t.unit_price,\n"
            + "     (t.unit_price*t.number) AS total_price\n"
            + "     FROM transactions t\n"
            + "     LEFT JOIN sources s ON t.source_id=s.id\n"
            + "     INNER JOIN expenses e ON e.id=t.expense_id\n"
            + "     INNER JOIN names n ON n.id = e.name_id\n"
            + "     INNER JOIN units u ON u.id = e.unit_id\n"
            + "     ORDER BY date DESC, source, expense, total_price;";


    public static void create(String url) {

        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + url, config.toProperties());
             Statement stmt = conn.createStatement()) {
            stmt.execute(SOURCES);
            System.out.println(SOURCES);
            stmt.execute(CATEGORIES);
            System.out.println(CATEGORIES);
            stmt.execute(NAMES);
            System.out.println(NAMES);
            stmt.execute(UNITS);
            System.out.println(UNITS);
            stmt.execute(EXPENSES);
            System.out.println(EXPENSES);
            stmt.execute(TAGS);
            System.out.println(TAGS);
            stmt.execute(TRANSACTIONS);
            System.out.println(TRANSACTIONS);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.err.println("Error: create database");
        }
    }
    public void clear(String url) throws SQLException {

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:"+url);
             Statement stmt = conn.createStatement()) {
            deleteAllFrom(conn,"transactions");
            deleteAllFrom(conn,"expenses");
            deleteAllFrom(conn,"units");
            deleteAllFrom(conn,"names");
            deleteAllFrom(conn,"categories");
            deleteAllFrom(conn,"sources");
            deleteAllFrom(conn,"tags");
        }

    }
    private static void deleteAllFrom(Connection conn, String tableName) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM " + tableName + ";");
        stmt.execute("UPDATE sqlite_sequence SET seq=0 WHERE name='" + tableName + "';");
    }
    public static void addSampleData(String url) {
        SourceDao sourceDao = new SourceDao(url);
        CategoryDao categoryDao = new CategoryDao(url);
        ExpenseDao expenseDao = new ExpenseDao(url);
        NameDao nameDao = new NameDao(url);
        UnitDao unitDao = new UnitDao(url);
        TransactionDao transactionDao = new TransactionDao((url));

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:"+url);
            deleteAllFrom(connection, "sources");
            Source biedronka = sourceDao.insert(new Source("Biedronka", "sklep wielobranżowy", "#121212"),connection);
            Source frac = sourceDao.insert(new Source("Frac", "sklep wielobranżowy", "#121212"),connection);
            Source denley = sourceDao.insert(new Source("Denley", "sklep wielobranżowy", "#121212"),connection);
            Source auchan = sourceDao.insert(new Source("Auchan", "sklep wielobranżowy", "#121212"),connection);
            Source decatlon = sourceDao.insert(new Source("Decatoln", "sklep wielobranżowy", "#121212"),connection);
            deleteAllFrom(connection, "categories");
            Category ubrania = categoryDao.insert(new Category("ODZIEŻ", "#121212"),connection);
            Category leki = categoryDao.insert(new Category("LEKARSTWA", "#121212"),connection);
            Category jedzenie = categoryDao.insert(new Category("ŻYWSNOŚĆ", "#121212"),connection);
            Category kosmetyki = categoryDao.insert(new Category("KOSMETYKI", "#121212"),connection);
            Category chemia = categoryDao.insert(new Category("CHEMIA", "#121212"),connection);
            Category elektronika = categoryDao.insert(new Category("ELEKTRONIKA", "#121212"),connection);
            deleteAllFrom(connection, "names");
            Name mleko = nameDao.insert(new Name("mleko", jedzenie),connection);
            Name papryka = nameDao.insert(new Name("papryka", jedzenie),connection);
            Name kielbasa = nameDao.insert(new Name("kiełbasa", jedzenie),connection);
            Name ananas = nameDao.insert(new Name("ananas", jedzenie),connection);
            Name bulka = nameDao.insert(new Name("bułka", jedzenie),connection);
            Name chleb = nameDao.insert(new Name("chleb", jedzenie),connection);
            Name maslo = nameDao.insert(new Name("masło", jedzenie),connection);
            Name rogal = nameDao.insert(new Name("rogal", jedzenie),connection);
            Name owsianka = nameDao.insert(new Name("owsianka", jedzenie),connection);
            Name ryz = nameDao.insert(new Name("ryż", jedzenie),connection);
            Name ryz_bronzowy = nameDao.insert(new Name("ryż brązowy", jedzenie),connection);
            Name kasza_jecz = nameDao.insert(new Name("kasza jęczmienna", jedzenie),connection);
            Name kasza_grycz = nameDao.insert(new Name("kasza gryczana", jedzenie),connection);
            Name szynka = nameDao.insert(new Name("szynka", jedzenie),connection);
            Name sok = nameDao.insert(new Name("sok", jedzenie),connection);
            Name woda = nameDao.insert(new Name("woda", jedzenie),connection);
            Name pizza = nameDao.insert(new Name("pizza", jedzenie),connection);
            Name kebab = nameDao.insert(new Name("kebab", jedzenie),connection);
            Name miod = nameDao.insert(new Name("miód", jedzenie),connection);
            Name seler = nameDao.insert(new Name("seler", jedzenie),connection);
            Name jablko = nameDao.insert(new Name("jabłko", jedzenie),connection);
            Name pomarancza = nameDao.insert(new Name("pomarańcza", jedzenie),connection);
            deleteAllFrom(connection, "units");
            Unit sztuka = unitDao.insert(new Unit("sztuka", "szt.", false),connection);
            Unit karton = unitDao.insert(new Unit("karton", null, false),connection);
            Unit butelka_2l = unitDao.insert(new Unit("butelka 2 litry", "but. 2l", false),connection);
            Unit butelka_1l = unitDao.insert(new Unit("butelka 1 litr", "but. 1l", false),connection);
            Unit kilogram = unitDao.insert(new Unit("kilogram", "kg.", true),connection);
            Unit laska = unitDao.insert(new Unit("laska", null, false),connection);
            Unit bochenek = unitDao.insert(new Unit("bochenek", null, false),connection);
            Unit kostka = unitDao.insert(new Unit("kostka", null, false),connection);
            Unit opakowanie = unitDao.insert(new Unit("opakowanie", "opk.", false),connection);
            Unit kawalek = unitDao.insert(new Unit("kawałek", null, false),connection);
            Unit sloik = unitDao.insert(new Unit("słoik", null, false),connection);
            Unit centrymetr = unitDao.insert(new Unit("centymetr", "cm", true),connection);
            Name apap = nameDao.insert(new Name("apap", leki),connection);
            Name espumisan = nameDao.insert(new Name("espumisan", leki),connection);
            Name plastry = nameDao.insert(new Name("plastry", leki),connection);
            Name woda_utl = nameDao.insert(new Name("woda utleniona", leki),connection);
            Name bandarze = nameDao.insert(new Name("bandarze", leki),connection);
            Unit metr = unitDao.insert(new Unit("metr", "m", true),connection);
            Name tshirt = nameDao.insert(new Name("t-shirt", ubrania),connection);
            Name longslave = nameDao.insert(new Name("longslave", ubrania),connection);
            Name sweter = nameDao.insert(new Name("sweter", ubrania),connection);
            Name spodnie = nameDao.insert(new Name("spodnie", ubrania),connection);
            Name buty = nameDao.insert(new Name("buty", ubrania),connection);
            Name bokserki = nameDao.insert(new Name("bokserki", ubrania),connection);
            Name maseczka = nameDao.insert(new Name("maseczka", ubrania),connection);
            Name skarpetki = nameDao.insert(new Name("skarpetki", ubrania),connection);
            Name pasek = nameDao.insert(new Name("pasek", ubrania),connection);
            Name czapka = nameDao.insert(new Name("czapka", ubrania),connection);
            Name kurtka = nameDao.insert(new Name("kurtka", ubrania),connection);
            Unit para = unitDao.insert(new Unit("para", null, false),connection);

            Name pianka_do_golenia = nameDao.insert(new Name("pianka do golenia", kosmetyki),connection);
            Name szampon = nameDao.insert(new Name("szampon", kosmetyki),connection);
            Name dezodornat = nameDao.insert(new Name("dezodorant", kosmetyki),connection);
            Name mydlo = nameDao.insert(new Name("mydło", kosmetyki),connection);
            Name krem = nameDao.insert(new Name("krem", kosmetyki),connection);
            Name golarka_jed = nameDao.insert(new Name("golarka jednorazowa", kosmetyki),connection);
            Name chusteczki = nameDao.insert(new Name("chusteczki", kosmetyki),connection);
            Name nic_dentystyczna = nameDao.insert(new Name("nić dentystyczna", kosmetyki),connection);

            Unit dziesieciopak = unitDao.insert(new Unit("dziesięciopak", "10-pak", false),connection);

            Name odswiezacz = nameDao.insert(new Name("odświżacz", chemia),connection);
            Name plyn_do_pr = nameDao.insert(new Name("płyn do prania", chemia),connection);
            Name plyn_do_zm = nameDao.insert(new Name("płyn do zmywania", chemia),connection);
            Name kostki_do_zmywarki = nameDao.insert(new Name("kostki do zmywarki", chemia),connection);
            Name plyn_do_podlogi = nameDao.insert(new Name("płyn do podłogi", chemia),connection);
            Name wybielacz = nameDao.insert(new Name("wybielacz", chemia),connection);

            Unit butelka_5l = unitDao.insert(new Unit("butelka 5 litrów", "but. 5l", false),connection);

            Name telefon = nameDao.insert(new Name("telefon", elektronika),connection);
            Name mp3 = nameDao.insert(new Name("oddwaracz mp3", elektronika),connection);
            Name baterie_alkaliczne = nameDao.insert(new Name("baterie alkaliczne", elektronika),connection);
            Name kabel_przedluzacz = nameDao.insert(new Name("kabel przedłużacz", elektronika),connection);
            Unit czteropak = unitDao.insert(new Unit("czteropak", "4-pak", false),connection);
            deleteAllFrom(connection, "expenses");
            Expense mleko_1l = expenseDao.insert(new Expense(mleko, butelka_1l),connection);
            Expense mleko_2l = expenseDao.insert(new Expense(mleko, butelka_2l),connection);
            Expense mleko_karton = expenseDao.insert(new Expense(mleko, karton), connection);
            Expense papryka_szt = expenseDao.insert(new Expense(papryka, sztuka), connection);
            Expense parpyka_kilo = expenseDao.insert(new Expense(papryka, kilogram), connection);
            Expense kielbasa_laska = expenseDao.insert(new Expense(kielbasa, laska), connection);
            Expense kielbasa_kilo = expenseDao.insert(new Expense(kielbasa, kilogram), connection);
            Expense ananas_szt = expenseDao.insert(new Expense(ananas, sztuka),connection);
            Expense bulka_szt = expenseDao.insert(new Expense(bulka, sztuka),connection);
            Expense chleb_boh = expenseDao.insert(new Expense(chleb, bochenek),connection);
            Expense maslo_kostka = expenseDao.insert(new Expense(maslo, kostka),connection);
            Expense rogal_szt = expenseDao.insert(new Expense(rogal, sztuka),connection);
            Expense owsianka_opk = expenseDao.insert(new Expense(owsianka, opakowanie),connection);
            Expense ryz_kilo = expenseDao.insert(new Expense(ryz, kilogram),connection);
            Expense ryz_opk = expenseDao.insert(new Expense(ryz, opakowanie),connection);
            Expense ryz_br_opk = expenseDao.insert(new Expense(ryz_bronzowy, opakowanie),connection);
            Expense kasza_gr_opk = expenseDao.insert(new Expense(kasza_grycz, opakowanie),connection);
            Expense kasza_je_opk = expenseDao.insert(new Expense(kasza_jecz, opakowanie),connection);
            Expense szynka_kilo = expenseDao.insert(new Expense(szynka, kilogram),connection);
            Expense szynka_opk = expenseDao.insert(new Expense(szynka, opakowanie),connection);
            Expense sok_but_2l = expenseDao.insert(new Expense(sok, butelka_2l),connection);
            Expense woda_but_1l = expenseDao.insert(new Expense(woda, butelka_1l),connection);
            Expense woda_but_5l = expenseDao.insert(new Expense(woda, butelka_5l),connection);
            Expense pizza_szt = expenseDao.insert(new Expense(pizza, sztuka),connection);
            Expense pizza_kawalek = expenseDao.insert(new Expense(pizza, kawalek),connection);
            Expense pizza_cm = expenseDao.insert(new Expense(pizza, centrymetr),connection);
            Expense kebab_szt = expenseDao.insert(new Expense(kebab, sztuka),connection);
            Expense miod_sloik = expenseDao.insert(new Expense(miod, sloik),connection);
            Expense seler_szt = expenseDao.insert(new Expense(seler, sztuka),connection);
            Expense jablko_szt = expenseDao.insert(new Expense(jablko, sztuka),connection);
            Expense jablko_kilo = expenseDao.insert(new Expense(jablko, kilogram),connection);
            Expense pomarancza_kilo = expenseDao.insert(new Expense(pomarancza, kilogram),connection);

            Expense apap_opk = expenseDao.insert(new Expense(apap, opakowanie),connection);
            Expense espumisan_opk = expenseDao.insert(new Expense(espumisan, opakowanie),connection);
            Expense plastry_opk = expenseDao.insert(new Expense(plastry, opakowanie),connection);
            Expense plastry_szt = expenseDao.insert(new Expense(plastry, sztuka),connection);
            Expense woda_utl_szt = expenseDao.insert(new Expense(woda_utl, sztuka),connection);
            Expense bandaz_metr = expenseDao.insert(new Expense(bandarze, metr),connection);

            Expense tshirt_szt = expenseDao.insert(new Expense(tshirt, sztuka),connection);
            Expense longslave_szt = expenseDao.insert(new Expense(longslave, sztuka),connection);
            Expense sweter_szt = expenseDao.insert(new Expense(sweter, sztuka),connection);
            Expense szpodni_szt = expenseDao.insert(new Expense(spodnie, sztuka),connection);
            Expense buty_par = expenseDao.insert(new Expense(buty, para),connection);
            Expense box_szt = expenseDao.insert(new Expense(bokserki, sztuka),connection);
            Expense mas_szt = expenseDao.insert(new Expense(maseczka, sztuka),connection);
            Expense mas_10pak = expenseDao.insert(new Expense(maseczka, dziesieciopak),connection);
            Expense skar_par = expenseDao.insert(new Expense(skarpetki, para),connection);
            Expense pasek_szt = expenseDao.insert(new Expense(pasek, sztuka),connection);
            Expense czapka_szt = expenseDao.insert(new Expense(czapka, sztuka),connection);
            Expense kurtka_szt = expenseDao.insert(new Expense(kurtka, sztuka),connection);

            Expense pianka_do_gol_szt = expenseDao.insert(new Expense(pianka_do_golenia, sztuka),connection);
            Expense szampon_szt = expenseDao.insert(new Expense(szampon, sztuka),connection);
            Expense dezodorant_szt = expenseDao.insert(new Expense(dezodornat, sztuka),connection);
            Expense mydlo_szt = expenseDao.insert(new Expense(mydlo, sztuka),connection);
            Expense mydlo_10pak = expenseDao.insert(new Expense(mydlo, dziesieciopak),connection);
            Expense krem_opk = expenseDao.insert(new Expense(krem, opakowanie),connection);
            Expense golarka_10pak = expenseDao.insert(new Expense(golarka_jed, dziesieciopak),connection);
            Expense chust_10pak = expenseDao.insert(new Expense(chusteczki, dziesieciopak),connection);
            Expense chust_opk = expenseDao.insert(new Expense(chusteczki, opakowanie),connection);
            Expense nic_opk = expenseDao.insert(new Expense(nic_dentystyczna, opakowanie),connection);
            Expense odswierzacz_opk = expenseDao.insert(new Expense(odswiezacz, opakowanie),connection);
            Expense plyn_do_pr_butelka5l = expenseDao.insert(new Expense(plyn_do_pr, butelka_5l),connection);
            Expense plyn_do_zmy_2l = expenseDao.insert(new Expense(plyn_do_zm, butelka_2l),connection);
            Expense kostki_do_zmy_opk = expenseDao.insert(new Expense(kostki_do_zmywarki, opakowanie),connection);
            Expense plyn_do_pod_5l = expenseDao.insert(new Expense(plyn_do_podlogi, butelka_5l),connection);
            Expense wybielacz_2l = expenseDao.insert(new Expense(wybielacz, butelka_2l),connection);

            Expense telefon_szt = expenseDao.insert(new Expense(telefon, sztuka),connection);
            Expense mp3_szt = expenseDao.insert(new Expense(mp3, sztuka),connection);
            Expense bat_szt = expenseDao.insert(new Expense(baterie_alkaliczne, sztuka),connection);
            Expense bat_4pak = expenseDao.insert(new Expense(baterie_alkaliczne, czteropak),connection);
            Expense kabel_metr = expenseDao.insert(new Expense(kabel_przedluzacz, metr),connection);
            Date today = new Date();

            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,mleko_1l,4, (float) 2.32, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,mleko_2l,3, (float) 5.65, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),frac,mleko_karton,10, (float) 4.54, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,papryka_szt,3, (float) 1.78, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,parpyka_kilo, (float) 2.432, (float) 1.54, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,kielbasa_laska, (float) 3.23, (float) 4.34, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,kielbasa_kilo, (float) 0.234, (float) 10.23, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,ananas_szt, (float) 1, (float) 5.45, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),auchan,ananas_szt, (float) 1, (float) 4.99, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,bulka_szt, (float) 1.89, (float) 12, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,chleb_boh, (float) 2, (float) 2.45, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,maslo_kostka, (float) 4.23, (float) 5, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,rogal_szt, (float) 1, (float) 1.9, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,owsianka_opk, (float) 3, (float) 1, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,ryz_kilo, (float) 4.5, (float) 14.99, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,ryz_opk, (float) 1, (float) 10.54, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,ryz_br_opk, (float) 2, (float) 12.54, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,kasza_gr_opk, (float) 4, (float) 13.24, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,kasza_je_opk, (float) 1, (float) 12.22, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,szynka_kilo, (float) 2.231, (float) 11.23, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,szynka_opk, (float) 2, (float) 6.34, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,sok_but_2l, (float) 2, (float) 5.34, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,woda_but_1l, (float) 1, (float) 0.78, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,woda_but_5l, (float) 2.432, (float) 2.34, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,pizza_szt, (float) 1, (float) 13.22, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,pizza_kawalek, (float) 2, (float) 5.34, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,pizza_cm, (float) 14, (float) 22.23, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,kebab_szt, (float) 1, (float) 8.00, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,miod_sloik, (float) 1, (float) 24.99, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,seler_szt, (float) 1, (float) 1.23, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,jablko_kilo, (float) 4.345, (float) 1.78, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,jablko_szt, (float) 2, (float) 2.00, null),connection);
            transactionDao.insert(new Transaction(DateConvertor.toLong(today),biedronka,pomarancza_kilo, (float) 2.543, (float) 2.33, null),connection);

            connection.close();
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }




    }

}

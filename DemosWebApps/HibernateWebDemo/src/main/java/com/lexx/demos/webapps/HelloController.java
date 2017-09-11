package com.lexx.demos.webapps;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getDefaultName(ModelMap model) {

        model.addAttribute("name", "this is default name");
        return "list";

    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String getName(@PathVariable String name, ModelMap model) {

        model.addAttribute("name", name);
        return "list";

    }

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public String getBookName(ModelMap model) {

        model.addAttribute("name", "Book");

        List<Book> bookList = BookManager.getBooks();
        for (Book book : bookList) {
            model.addAttribute("title", book.getTitle());
            model.addAttribute("author", book.getAuthor());
            model.addAttribute("isbn", book.getIsbn());
        }

        return "list";

    }

}
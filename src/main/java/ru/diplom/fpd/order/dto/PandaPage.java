package ru.diplom.fpd.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class PandaPage<T> {

    private int totalPages;
    private int number;
    private int size;
    private List<T> content;

    @JsonIgnore
    private Page<T> page;

    private PandaPage(Page<T> page) {
        this.totalPages = page.getTotalPages();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.content = page.getContent();
        this.page = page;
    }

    public static <T> PandaPage<T> of(Page<T> page) {
        return new PandaPage<T>(page);
    }
}

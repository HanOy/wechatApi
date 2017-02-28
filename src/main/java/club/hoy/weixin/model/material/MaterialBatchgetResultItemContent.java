package club.hoy.weixin.model.material;

import java.util.List;

import club.hoy.weixin.model.Article;

public class MaterialBatchgetResultItemContent {

	private List<Article> news_item;
	
	private String create_time;
    
    private String update_time;

	public String getCreate_time() {
        return create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public List<Article> getNews_item() {
		return news_item;
	}

	public void setNews_item(List<Article> news_item) {
		this.news_item = news_item;
	}


}

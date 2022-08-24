package konkuk.nServer.domain.common.dto.response;

import konkuk.nServer.domain.proposal.domain.ProposalDetail;
import lombok.Data;

import java.util.List;

@Data
public class FindOrderMenu {

    private List<Menu> myOrder;
    private List<OtherMenu> others;


    public static class OtherMenu {
        public String nickname;
        public List<Menu> menus;

        public OtherMenu(String nickname, List<Menu> menus) {
            this.nickname = nickname;
            this.menus = menus;
        }
    }


    public static class Menu {
        public String foodName;
        public Integer price;

        public Menu(String foodName, Integer price) {
            this.foodName = foodName;
            this.price = price;
        }
    }

    public FindOrderMenu(List<Menu> myOrder, List<OtherMenu> others) {
        this.myOrder = myOrder;
        this.others = others;
    }
}

package konkuk.nServer.domain.store.domain;

import konkuk.nServer.domain.post.domain.Category;
import konkuk.nServer.domain.user.domain.Storemanager;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Integer deliveryFee;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String businessHours;

    private String breakTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storemanager_id")
    private Storemanager storemanager;

    @Enumerated(EnumType.STRING)
    private StoreState state;

    @Builder
    public Store(String name, String phone, Integer deliveryFee, String address, String businessHours,
                 String breakTime, Storemanager storemanager, StoreState state, Category category) {
        this.name = name;
        this.phone = phone;
        this.deliveryFee = deliveryFee;
        this.address = address;
        this.businessHours = businessHours;
        this.breakTime = breakTime;
        this.storemanager = storemanager;
        this.state = state;
        this.category = category;
    }

    public void addMenu(Menu menu) {
        this.menus.add(menu);
        menu.setStore(this);
    }


}

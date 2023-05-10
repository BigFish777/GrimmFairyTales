package cn.apotato.test.jpa.service;

import cn.apotato.test.jpa.domain.Pet;
import cn.apotato.test.jpa.repository.PetRepository;
import org.springframework.stereotype.Service;

/**
 * 宠物服务
 *
 * @author 胡晓鹏
 * @date 2023/05/09
 */
@Service
public class PetService extends BaseService<Pet> {
    public PetService(PetRepository repository) {
        super(repository);
    }

    /**
     * 保存
     *
     * @param pet 宠物
     * @return {@link Pet}
     */
    public Pet save(Pet pet) {
        this.repository.save(pet);
        return pet;
    }

}

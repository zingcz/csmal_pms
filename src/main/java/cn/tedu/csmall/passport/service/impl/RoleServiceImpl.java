package cn.tedu.csmall.passport.service.impl;

import cn.tedu.csmall.passport.mapper.RoleMapper;
import cn.tedu.csmall.passport.pojo.vo.RoleListItemVO;
import cn.tedu.csmall.passport.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Transactional
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    RoleMapper roleMapper;
    public List<RoleListItemVO> list(){
        List<RoleListItemVO> list = roleMapper.list();

        Iterator<RoleListItemVO> iterator = list.iterator();
        while (iterator.hasNext()) {
            RoleListItemVO roleList = iterator.next();
            if (roleList.getId() == 1) {
                iterator.remove();
                break;
            }
        }
        return list;
    }
}

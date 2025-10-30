import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/loginView.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/loginView.vue')
    },
    {
      path: '/sliceCageDemo',
      name: 'sliceCageDemo',
      component: () => import('../views/sliceCageDemo.vue')
    },
    {
      path: '/main',
      name: 'main',
      component: () => import('../layout/mainErpView.vue'),
      children: [
        /*----------- 测试管理页面 ----------------*/
        {
          path: 'plcTest',
          name: 'plcTest',
          component: () => import('../views/plcTest/return.vue'),
          children: [
            {
              path: '/plcTest/Test',
              name: 'plcTest',
              component: () => import('../views/plcTest/Test.vue')
            },
          ]
        },

        /*----------- 上片机 ----------------*/
        {
          path: 'upperComputer',
          name: 'upper-computer',
          component: () => import('../views/upperComputer/return.vue'),
          children: [
            {
              path: '/upperComputer/loadGlassOne',
              name: 'loadGlassOne',
              component: () => import('../views/upperComputer/loadGlassOne.vue')
            },
            {
              path: '/upperComputer/loadGlassTwo',
              name: 'loadGlassTwo',
              component: () => import('../views/upperComputer/loadGlassTwo.vue')
            },
            {
              path: '/upperComputer/loadGlassOneLY',
              name: 'loadGlassOneLY',
              component: () => import('../views/upperComputer/loadGlassOneLY.vue')
            },
            {
              path: '/upperComputer/loadGlassTwoLY',
              name: 'loadGlassTwoLY',
              component: () => import('../views/upperComputer/loadGlassTwoLY.vue')
            }
          ]
        },
        /*----------- 掰片/识别 ----------------*/
        {
          path: 'breakPiece',
          name: 'break-piece',
          component: () => import('../views/breakPiece/return.vue'),
          children: [
            {
              path: '/breakPiece/brokenPieceInquiry',
              name: 'brokenPieceInquiry',
              component: () => import('../views/breakPiece/brokenPieceInquiry.vue')
            },
            {
              path: '/breakPiece/breakPieceOne',
              name: 'breakPieceOne',
              component: () => import('../views/breakPiece/breakPieceOne.vue')
            },
            {
              path: '/breakPiece/breakPieceTwo',
              name: 'breakPieceTwo',
              component: () => import('../views/breakPiece/breakPieceTwo.vue')
            }
          ]
        },
        /*----------- 卧式缓存 ----------------*/
        {
          path: 'horizontalCaching',
          name: 'horizontal-caching',
          component: () => import('../views/horizontalCaching/return.vue'),
          children: [
            {
              path: '/horizontalCaching/horizontalCachingOne',
              name: 'horizontalCachingOne',
              component: () => import('../views/horizontalCaching/horizontalCachingOne.vue')
            },
            {
              path: '/horizontalCaching/horizontalCachingTwo',
              name: 'horizontalCachingTwo',
              component: () => import('../views/horizontalCaching/horizontalCachingTwo.vue')
            }
          ]
        },
        /*----------- 磨边（冷加工） ----------------*/
        {
          path: 'edging',
          name: 'edging',
          component: () => import('../views/edging/return.vue'),
          children: [
            {
              path: '/edging/edgingOne',
              name: 'edgingOne',
              component: () => import('../views/edging/edgingOne.vue')
            },
            {
              path: '/edging/edgingOneLY',
              name: 'edgingOneLY',
              component: () => import('../views/edging/edgingOneLY.vue')
            }
          ]
        },
        /*----------- 大理片笼 ----------------*/
        {
          path: 'daliSliceCage',
          name: 'dali-slice-cage',
          component: () => import('../views/daliSliceCage/return.vue'),
          children: [
            {
              path: '/daliSliceCage/sliceCage',
              name: 'sliceCage',
              component: () => import('../views/daliSliceCage/sliceCage.vue')
            },
            {
              path: '/daliSliceCage/sliceCageLY',
              name: 'sliceCageLY',
              component: () => import('../views/daliSliceCage/sliceCageLY.vue')
            }
          ]
        },
        /*----------- 钢化 ----------------*/
        {
          path: 'steel',
          name: 'steel',
          component: () => import('../views/steel/return.vue'),
          children: [
            // 钢化查询
            {
              path: '/steel/temperedPages',
              name: 'temperedPages',
              component: () => import('../views/steel/temperedPages.vue')
            },
            // 钢化前
            {
              path: '/steel/temperingBefore',
              name: 'temperingBefore',
              component: () => import('../views/steel/temperingBefore.vue')
            },
            // 钢化中
            {
              path: '/steel/temperingOngoing',
              name: 'temperingOngoing',
              component: () => import('../views/steel/temperingOngoing.vue')
            },
            // 钢化后
            {
              path: '/steel/temperingAfter',
              name: 'temperingAfter',
              component: () => import('../views/steel/temperingAfter.vue')
            },
            // 洛阳钢化前中
            {
              path: '/steel/temperingBeOnLY',
              name: 'temperingBeOnLY',
              component: () => import('../views/steel/temperingBeOnLY.vue')
            },
            // 洛阳钢化后
            {
              path: '/steel/temperingAfterLY',
              name: 'temperingAfterLY',
              component: () => import('../views/steel/temperingAfterLY.vue')
            },
            // 洛阳钢化查询
            {
              path: '/steel/temperedPagesLY',
              name: 'temperedPagesLY',
              component: () => import('../views/steel/temperedPagesLY.vue')
            },
            // 洛阳钢化线
            {
              path: '/steel/temperedLineLY',
              name: 'temperedLineLY',
              component: () => import('../views/steel/temperedLineLY.vue')
            },
          ]
        },
        /*----------- 系统管理 ----------------*/
        {
          path: 'systemAdministration',
          name: 'system-administration',
          component: () => import('../views/systemAdministration/return.vue'),
          children: [
            // 用户管理
            {
              path: '/systemAdministration/userManagement',
              name: 'userManagement',
              component: () => import('../views/systemAdministration/userManagement.vue')
            },
            // 权限管理
            {
              path: '/systemAdministration/permissionsManagement',
              name: 'permissionsManagement',
              component: () => import('../views/systemAdministration/permissionsManagement.vue')
            },
            // 角色管理
            {
              path: '/systemAdministration/roleManagement',
              name: 'roleManagement',
              component: () => import('../views/systemAdministration/roleManagement.vue')
            },
            // 系统配置
            {
              path: '/systemAdministration/systemConfiguration',
              name: 'systemConfiguration',
              component: () => import('../views/systemAdministration/systemConfiguration.vue')
            }
          ]
        },
        //  /*----------- 仓储中心 ----------------*/
        {
          path: 'warehousingCenters',
          name: 'warehousing-centers',
          component: () => import('../views/warehousingCenters/return.vue'),
          children: [
            {
              path: '/warehousingCenters/rawFilmStorage',
              name: 'rawFilmStorage',
              component: () => import('../views/warehousingCenters/rawFilmStorage.vue')
            },
            {
              path: '/warehousingCenters/rawFilmStorageLY',
              name: 'rawFilmStorageLY',
              component: () => import('../views/warehousingCenters/rawFilmStorageLY.vue')
            }
          ]
        },
        /*----------- 报工管理 ----------------*/
        {
          path: 'reportManagement',
          name: 'report-management',
          component: () => import('../views/reportManagement/signingWork.vue'),
          children: [
            {
              path: '/reportManagement/signingWork',
              name: 'signingWork',
              component: () => import('../views/reportManagement/signingWork.vue')
            }
          ]
        },
        /*----------- 中空 ----------------*/
        {
          path: 'hollow',
          name: 'hollow',
          component: () => import('../views/hollow/return.vue'),
          children: [
            {
              path: '/hollow/hollowLineOne',
              name: 'hollowLineOne',
              component: () => import('../views/hollow/hollowLineOne.vue')
            },
            {
              path: '/hollow/hollowLineTwo',
              name: 'hollowLineTwo',
              component: () => import('../views/hollow/hollowLineTwo.vue')
            },
            {
              path: '/hollow/hollowLineThree',
              name: 'hollowLineThree',
              component: () => import('../views/hollow/hollowLineThree.vue')
            },
            {
              path: '/hollow/hollowCage',
              name: 'hollowCage',
              component: () => import('../views/hollow/hollowCage.vue')
            },
            {
              path: '/hollow/foldedOne',
              name: 'foldedOne',
              component: () => import('../views/hollow/foldedOne.vue')
            },
            {
              path: '/hollow/foldedTwo',
              name: 'foldedTwo',
              component: () => import('../views/hollow/foldedTwo.vue')
            },
            {
              path: '/hollow/foldedThree',
              name: 'foldedThree',
              component: () => import('../views/hollow/foldedThree.vue')
            }
          ]
        },
        /*----------- 大屏显示 ----------------*/
        {
          path: 'largeScreenDisplay',
          name: 'large-screen-display',
          component: () => import('../views/largeScreenDisplay/screenDisplay.vue'),
          children: [
            {
              path: '/largeScreenDisplay/screenDisplay',
              name: 'screenDisplay',
              component: () => import('../views/largeScreenDisplay/screenDisplay.vue')
            }
          ]
        },
      ]
    },
    // 上片机--历史任务
    {
      path: '/loadHistory',
      name: 'loadHistory',
      component: () => import('../views/upperComputer/loadHistory.vue'),
      children: [
        {
          path: '/upperComputer/loadHistory',
          name: 'loadHistory',
          component: () => import('../views/upperComputer/loadHistory.vue')
        },
      ]
    },
    // 原片仓储--历史任务
    {
      path: '/rawHistory',
      name: 'rawHistory',
      component: () => import('../views/warehousingCenters/rawHistory.vue'),
      children: [
        {
          path: '/warehousingCenters/rawHistory',
          name: 'rawHistory',
          component: () => import('../views/warehousingCenters/rawHistory.vue')
        },
      ]
    },
    // 原片仓储--亚美尼亚图片
    {
      path: '/rawGlassLayout',
      name: 'rawGlassLayout',
      component: () => import('../views/warehousingCenters/dynamicComponents/rawGlassLayout.vue'),
      children: [
        {
          path: '/warehousingCenters/rawGlassLayout',
          name: 'rawGlassLayout',
          component: () => import('../views/warehousingCenters/dynamicComponents/rawGlassLayout.vue')
        },
      ]
    },
    // 原片仓储--洛阳图片
    {
      path: '/rawGlassLayoutLY',
      name: 'rawGlassLayoutLY',
      component: () => import('../views/warehousingCenters/dynamicComponents/rawGlassLayoutLY.vue'),
      children: [
        {
          path: '/warehousingCenters/rawGlassLayoutLY',
          name: 'rawGlassLayoutLY',
          component: () => import('../views/warehousingCenters/dynamicComponents/rawGlassLayoutLY.vue')
        },
      ]
    },
    // 磨边--历史任务
    {
      path: '/edgingHistory',
      name: 'edgingHistory',
      component: () => import('../views/edging/edgingHistory.vue'),
      children: [
        {
          path: '/edging/edgingHistory',
          name: 'edgingHistory',
          component: () => import('../views/edging/edgingHistory.vue')
        },
      ]
    },
    // 大理片笼--历史任务
    {
      path: '/sliceCageHistory',
      name: 'sliceCageHistory',
      component: () => import('../views/daliSliceCage/sliceCageHistory.vue'),
      children: [
        {
          path: '/daliSliceCage/sliceCageHistory',
          name: 'sliceCageHistory',
          component: () => import('../views/daliSliceCage/sliceCageHistory.vue')
        },
      ]
    },
    // 大理片笼--钢化查询-钢化任务
    {
      path: '/sliceTemperingTasks',
      name: 'sliceTemperingTasks',
      component: () => import('../views/daliSliceCage/sliceTemperingTasks.vue'),
      children: [
        {
          path: '/daliSliceCage/sliceTemperingTasks',
          name: 'sliceTemperingTasks',
          component: () => import('../views/daliSliceCage/sliceTemperingTasks.vue')
        },
      ]
    },
    // 中空线--历史任务
    {
      path: '/basicHollowLineHistory',
      name: 'basicHollowLineHistory',
      component: () => import('../views/hollow/basicHollowLineHistory.vue'),
      children: [
        {
          path: '/hollow/basicHollowLineHistory',
          name: 'basicHollowLineHistory',
          component: () => import('../views/hollow/basicHollowLineHistory.vue')
        },
      ]
    },
    // 中空理片籠--历史任务
    {
      path: '/hollowCageHistory',
      name: 'hollowCageHistory',
      component: () => import('../views/hollow/hollowCageHistory.vue'),
      children: [
        {
          path: '/hollow/hollowCageHistory',
          name: 'hollowCageHistory',
          component: () => import('../views/hollow/hollowCageHistory.vue')
        },
      ]
    },
    // 卧理--历史任务
    {
      path: '/horizontalCaching/horizontalHistory',
      name: 'horizontalHistory',
      component: () => import('../views/horizontalCaching/horizontalHistory.vue'),
      children: [
        {
          path: '/horizontalCaching/horizontalHistory',
          name: 'horizontalHistory',
          component: () => import('../views/horizontalCaching/horizontalHistory.vue')
        },
      ]
    },
  ]
})
// 导航守卫  
router.beforeEach((to, from, next) => {
  const isAuthenticated = !!localStorage.getItem('authToken'); // 假设这是你的登录状态检查逻辑
  if (to.matched.some(record => record.meta.requiresAuth) && !isAuthenticated) {
    next({ name: 'login' }); // 如果需要登录但未登录，则重定向到登录页面
  } else {
    next();
  }
});
export default router
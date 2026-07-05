// Product image key -> Font Awesome icon mapping
const iconMap = {
  tshirt: 'fa-tshirt',
  bag: 'fa-shopping-bag',
  headphone: 'fa-headphones',
  watch: 'fa-clock-o',
  sunglass: 'fa-sun-o',
  phonecase: 'fa-mobile',
  shoe: 'fa-shoe',
  camera: 'fa-camera',
  pants: 'fa-pied-piper',
  dress: 'fa-female',
  totebag: 'fa-shopping-bag',
  cap: 'fa-user-secret',
}

export function getProductIcon(imageKey) {
  return iconMap[imageKey] || 'fa-cube'
}

export const badgeColors = {
  hot: '#e74c3c',
  new: '#27ae60',
}
